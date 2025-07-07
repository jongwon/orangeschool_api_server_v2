package com.orangeschool.orangeschoolapiserver.domain.commonMember;

import com.google.firebase.messaging.Notification;
import com.orangeschool.orangeschoolapiserver.common.dto.request.LoginDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.SocialLoginDto;
import com.orangeschool.orangeschoolapiserver.common.dto.request.UpdateActivationDto;
import com.orangeschool.orangeschoolapiserver.common.dto.response.AuthDto;
import com.orangeschool.orangeschoolapiserver.common.enums.JoinType;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import com.orangeschool.orangeschoolapiserver.common.utils.FileManagement;
import com.orangeschool.orangeschoolapiserver.common.utils.JwtTokenProvider;
import com.orangeschool.orangeschoolapiserver.common.utils.SmsManagement;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.dto.*;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.entity.CommonMember;
import com.orangeschool.orangeschoolapiserver.domain.commonMember.repository.CommonMemberRepository;
import com.orangeschool.orangeschoolapiserver.domain.location.LocationService;
import com.orangeschool.orangeschoolapiserver.domain.memberAlarm.MemberAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommonMemberService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CommonMemberRepository commonMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileManagement fileManagement;
    private final LocationService locationService;
    private final SmsManagement smsManagement;

    private final MemberAlarmService memberAlarmService;

    @Transactional
    public Long create(CreateCommonMemberDto createCommonMemberDto, MultipartFile file) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByEmail(createCommonMemberDto.getEmail());

        if (commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.CONFLICT_ACCOUNT);
        }

        if (createCommonMemberDto.getMemberType() == MemberType.PARENT) {
            checkNickName(createCommonMemberDto.getParentNickName());
        }

        CommonMember commonMember = CommonMember.builder()
                .joinType(createCommonMemberDto.getJoinType())
                .memberType(createCommonMemberDto.getMemberType())
                .email(createCommonMemberDto.getEmail())
                .name(createCommonMemberDto.getName())
                .birth(createCommonMemberDto.getBirth())
                .gender(createCommonMemberDto.getGender())
                .genderTitle(createCommonMemberDto.getGender() == 1 || createCommonMemberDto.getGender() == 3 ? "남성" : "여성")
                .phoneNumber(createCommonMemberDto.getPhoneNumber())
                .address(createCommonMemberDto.getAddress())
                .addressDetail(createCommonMemberDto.getAddressDetail())
                .agreeToSms(createCommonMemberDto.getAgreeToSms())
                .agreeToService(createCommonMemberDto.getAgreeToService())
                .agreeToAd(createCommonMemberDto.getAgreeToAd())
                .agreeToSchedule(true)
                .pushToken(createCommonMemberDto.getPushToken())
                .socialToken(createCommonMemberDto.getSocialToken())
                .isActive(true)
                .password(passwordEncoder.encode(createCommonMemberDto.getPassword()))
                .myReferralCode(generateUniqueReferralCode())
                .referralCodeTemp(createCommonMemberDto.getReferralCode())
                .parentNickName(createCommonMemberDto.getParentNickName())
                .nickName(createCommonMemberDto.getNickName())
                .intro(createCommonMemberDto.getIntro())
                .schoolName(createCommonMemberDto.getSchoolName())
                .grade(createCommonMemberDto.getGrade())
                .schoolClass(createCommonMemberDto.getSchoolClass())
                .schoolCode(createCommonMemberDto.getSchoolCode())
                .classNumber(createCommonMemberDto.getClassNumber())
                .parentId(createCommonMemberDto.getParentId() == 0L ? 0L : createCommonMemberDto.getParentId())
                .parentName(createCommonMemberDto.getParentId() == 0L ? "" : createCommonMemberDto.getName())
                .locationCode(locationService.getLocationCode(createCommonMemberDto.getAddress()))
                .challengeProgress(false)
                .totalOrange(0)
                .serverFileName("")
                .originFileName("")
                .fileUrl("")
                .build();

        if (file != null) {
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            commonMember.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        if(createCommonMemberDto.getReferralCode() != null && !createCommonMemberDto.getReferralCode().isEmpty()) {
            Optional<CommonMember> referralMemberOptional = commonMemberRepository.findByMyReferralCode(createCommonMemberDto.getReferralCode());

            if (referralMemberOptional.isEmpty()) {
                throw new CustomException(ResponseCode.NOTFOUND_REFERRAL_CODE);
            }

            Notification notification = Notification.builder()
                    .setTitle("구성원알림")
                    .setBody("새로운 구성원이 참여 요청하였습니다. 마이>구성원 관리에서 승인해 주세요.")
                    .build();

            memberAlarmService.createToSystem("구성원알림", "새로운 구성원이 참여 요청하였습니다. 마이>구성원 관리에서 승인해 주세요.", notification, referralMemberOptional.get());
        }

        return commonMemberRepository.save(commonMember).getId();
    }

    @Transactional(readOnly = true)
    public void checkNickName(String parentNickName) throws Exception {
        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByParentNickName(parentNickName);

        if (commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.CONFLICT_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public void checkEmail(CheckEmailDto checkEmailDto) throws Exception {
        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByEmail(checkEmailDto.getEmail());

        if (commonMemberOptional.isPresent()) {
            throw new CustomException(ResponseCode.CONFLICT_ACCOUNT);
        }
    }

//    @Transactional(readOnly = true)
//    public RandomNumberDto checkPhoneNumber(CheckPhoneNumberDto checkPhoneNumberDto) throws Exception {
//
//        if(checkPhoneNumberDto.getIsAdmin() == null) {
//            Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByPhoneNumber(checkPhoneNumberDto.getPhoneNumber());
//
//            if (commonMemberOptional.isPresent()) {
//                throw new CustomException(ResponseCode.CONFLICT_PHONE_NUMBER);
//            }
//        }
//
//        Random random = new Random();
//
//        //난수 생성
//        String randomNum = "";
//        for (int i = 0; i < 6; i++) {
//            randomNum += Integer.toString(random.nextInt(9));
//        }
//
//        //sms전송
//        smsManagement.send(checkPhoneNumberDto.getPhoneNumber(), "[오렌지스쿨] 인증번호를 입력해주세요: " + randomNum);
//
//        // 관리자일 경우 인증번호 저장
//        if(checkPhoneNumberDto.getIsAdmin() != null) {
//            String key = randomNum;  // "123456"
//            String value = randomNum; // "123456"
//
//            // Redis에 데이터 저장 (TTL 5분 설정)
//            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));
//        }
//
//        return RandomNumberDto.create(randomNum);
//    }
    @Transactional(readOnly = true)
    public RandomNumberDto checkPhoneNumber(CheckPhoneNumberDto checkPhoneNumberDto) throws Exception {

        //난수 생성
        String randomNum = "1111";

        // 관리자일 경우 인증번호 저장
        if(checkPhoneNumberDto.getIsAdmin() != null) {
            String key = randomNum;  // "123456"
            String value = randomNum; // "123456"

            // Redis에 데이터 저장 (TTL 5분 설정)
            redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(5));
        }

        return RandomNumberDto.create(randomNum);
    }


    @Transactional(readOnly = true)
    public RandomNumberDto findEmailCheckPhoneNumber(CheckPhoneNumberDto checkPhoneNumberDto) throws Exception {

        Random random = new Random();

        //난수 생성
        String randomNum = "";
        for (int i = 0; i < 6; i++) {
            randomNum += Integer.toString(random.nextInt(9));
        }

        //sms전송
        smsManagement.send(checkPhoneNumberDto.getPhoneNumber(), "[오렌지스쿨] 인증번호를 입력해주세요: " + randomNum);

        return RandomNumberDto.create(randomNum);
    }

    @Transactional
    public AuthDto login(LoginDto loginDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByEmail(loginDto.getAccount());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), commonMemberOptional.get().getPassword())) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_ACCOUNT);
        }

        if (!commonMemberOptional.get().getIsActive()) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_DORMANCY);
        }

        CommonMember commonMember = commonMemberOptional.get();

        if (!loginDto.getPushToken().isEmpty()) {
            commonMemberRepository.deletePushToken(loginDto.getPushToken(), loginDto.getAccount());
            commonMember.setPushToken(loginDto.getPushToken());
            commonMemberRepository.save(commonMember);
        }

        if (commonMember.getMyReferralCode() == null || commonMember.getMyReferralCode() == "") {
            commonMember.updateMyReferralCode(generateUniqueReferralCode());
        }

        AuthDto authDto = new AuthDto();
        authDto.setId(commonMemberOptional.get().getId());
        authDto.setAccessToken(jwtTokenProvider.createAccessToken(commonMemberOptional.get().getId(), "ROLE_USER"));

        return authDto;
    }

    @Transactional
    public AuthDto loginSocial(SocialLoginDto socialLoginDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByJoinTypeAndSocialTokenAndIsActive(
                socialLoginDto.getJoinType(), socialLoginDto.getSocialToken(), true);


        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        if (!socialLoginDto.getPushToken().isEmpty()) {
            commonMemberRepository.deletePushToken(socialLoginDto.getPushToken(), commonMember.getEmail());
            commonMember.setPushToken(socialLoginDto.getPushToken());
            commonMemberRepository.save(commonMember);
        }

        if (commonMember.getMyReferralCode() == null || commonMember.getMyReferralCode() == "") {
            commonMember.updateMyReferralCode(generateUniqueReferralCode());
        }

        AuthDto authDto = new AuthDto();
        authDto.setId(commonMemberOptional.get().getId());
        authDto.setAccessToken(jwtTokenProvider.createAccessToken(commonMemberOptional.get().getId(), "ROLE_USER"));

        return authDto;
    }

    @Transactional(readOnly = true)
    public FindEmailResponseDto findEmail(FindEmailRequestDto findEmailRequestDto) throws Exception {

        List<CommonMember> commonMemberList = commonMemberRepository.findByNameAndPhoneNumber(findEmailRequestDto.getName(), findEmailRequestDto.getPhoneNumber());
        List<String> emailList = commonMemberList.stream().map(CommonMember::getEmail).collect(Collectors.toList());

        return FindEmailResponseDto.builder().emailList(emailList).build();
    }

    @Transactional(readOnly = true)
    public void findPassword(FindPasswordRequestDto findPasswordRequestDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByEmailAndNameAndPhoneNumber(findPasswordRequestDto.getEmail(), findPasswordRequestDto.getName(), findPasswordRequestDto.getPhoneNumber());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findByEmail(resetPasswordRequestDto.getEmail());

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        commonMember.updatePassword(passwordEncoder.encode(resetPasswordRequestDto.getPassword()));

        commonMemberRepository.save(commonMember);
    }

    @Transactional(readOnly = true)
    public Page<CommonMemberDto> get(Pageable pageable, CommonMemberFilterDto commonMemberFilter) throws Exception {
        return commonMemberRepository.search(pageable, commonMemberFilter);
    }

    @Transactional(readOnly = true)
    public List<CommonMemberDto> getChildListToUser(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }
        CommonMember commonMember = commonMemberOptional.get();

        List<CommonMember> commonMemberList = new ArrayList<>();

        // 초대코드 등록된 경우
        if (commonMember.getReferralCode() != null && !commonMember.getReferralCode().isEmpty()) {
            commonMemberList = commonMemberRepository.findByParentIdOrReferralCode(commonMemberId, commonMember.getReferralCode());
        }

        // 초대코드 등록 안 된 경우
        if (commonMember.getReferralCode() == null || commonMember.getReferralCode().isEmpty()) {
            commonMemberList = commonMemberRepository.findByParentId(commonMemberId);
        }

        return commonMemberList.stream().map(CommonMemberDto::create).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CommonMemberDto> getChildList(Long commonMemberId, Pageable pageable) throws Exception {

        return commonMemberRepository.searchByParentId(commonMemberId, pageable);
    }

    @Transactional(readOnly = true)
    public CommonMemberDto getById(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        CommonMemberDto commonMemberDto = CommonMemberDto.create(commonMember);

        if (commonMember.getParentId() != 0L) {
            Optional<CommonMember> parentMemberOptional = commonMemberRepository.findById(commonMember.getParentId());
            parentMemberOptional.ifPresent(member -> commonMemberDto.setParentInfo(CommonMemberDto.create(member)));
        }

        return commonMemberDto;
    }

    @Transactional(readOnly = true)
    public CommonMemberDto getMyInfo(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        CommonMemberDto commonMemberDto = CommonMemberDto.create(commonMember);

        if (commonMember.getParentId() != 0L) {
            Optional<CommonMember> parentMemberOptional = commonMemberRepository.findById(commonMember.getParentId());
            parentMemberOptional.ifPresent(member -> commonMemberDto.setParentInfo(CommonMemberDto.create(member)));
        }

        return commonMemberDto;
    }

    @Transactional(readOnly = true)
    public CommonMemberDto getChildInfo(Long parentId, Long childId) throws Exception {

        CommonMember child = checkMyChild(parentId, childId);

        CommonMemberDto commonMemberDto = CommonMemberDto.create(child);

        if (child.getParentId() != 0L) {
            Optional<CommonMember> parentMemberOptional = commonMemberRepository.findById(child.getParentId());
            parentMemberOptional.ifPresent(commonMember -> commonMemberDto.setParentInfo(CommonMemberDto.create(commonMember)));
        }

        return commonMemberDto;
    }

    @Transactional
    public void put(Long commonMemberId, UpdateCommonMemberDto updateCommonMemberDto, MultipartFile file) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        commonMember.updateToAddress(
                updateCommonMemberDto.getName(),
                updateCommonMemberDto.getBirth(),
                updateCommonMemberDto.getGender(),
                updateCommonMemberDto.getGender() == 1 ? "남성" : "여성",
                updateCommonMemberDto.getNickName(),
                updateCommonMemberDto.getIntro(),
                updateCommonMemberDto.getAddress(),
                updateCommonMemberDto.getAddressDetail(),
                locationService.getLocationCode(updateCommonMemberDto.getAddress())
        );

        if (updateCommonMemberDto.getDeleteFileFlag()) {
            commonMember.deleteFile();
        }

        if (file != null) {
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            commonMember.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void putMyInfo(Long commonMemberId, UpdateCommonMemberDtoForApp updateCommonMemberDtoForApp, MultipartFile file) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        commonMember.updateToAddress(
                updateCommonMemberDtoForApp.getName(),
                updateCommonMemberDtoForApp.getBirth(),
                updateCommonMemberDtoForApp.getGender(),
                updateCommonMemberDtoForApp.getGender() == 1 ? "남성" : "여성",
                updateCommonMemberDtoForApp.getNickName(),
                updateCommonMemberDtoForApp.getIntro(),
                updateCommonMemberDtoForApp.getAddress(),
                updateCommonMemberDtoForApp.getAddressDetail(),
                locationService.getLocationCode(updateCommonMemberDtoForApp.getAddress())
        );

        if (updateCommonMemberDtoForApp.getChangeParentNickName()) {
            checkNickName(updateCommonMemberDtoForApp.getParentNickName());

            commonMember.updateParentNickname(updateCommonMemberDtoForApp.getParentNickName());
        }

        commonMember.updatePhoneNumber(updateCommonMemberDtoForApp.getPhoneNumber());

        if (updateCommonMemberDtoForApp.getChangeAddress()) {
            commonMember.updateAddress(
                    updateCommonMemberDtoForApp.getAddress(),
                    updateCommonMemberDtoForApp.getAddressDetail(),
                    locationService.getLocationCode(updateCommonMemberDtoForApp.getAddress())
            );

            // 부모일 경우 자녀 주소 전부 변경
            if (commonMember.getParentId() == 0L) {
                List<CommonMember> childList = commonMemberRepository.findByParentId(commonMemberId);
                for (int i = 0; i < childList.size(); i++) {
                    CommonMember child = childList.get(i);
                    child.updateAddress(
                            updateCommonMemberDtoForApp.getAddress(),
                            updateCommonMemberDtoForApp.getAddressDetail(),
                            locationService.getLocationCode(updateCommonMemberDtoForApp.getAddress())
                    );
                    commonMemberRepository.save(child);
                }
            }
        }

        if (updateCommonMemberDtoForApp.getDeleteFileFlag()) {
            commonMember.deleteFile();
        }

        if (file != null) {
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            commonMember.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        if (updateCommonMemberDtoForApp.getChangeEmail()) {
            if (commonMember.getJoinType() != JoinType.NORMAL) {
                throw new CustomException(ResponseCode.BAD_REQUEST);
            }

            Optional<CommonMember> checkEmailOptional = commonMemberRepository.findByEmail(updateCommonMemberDtoForApp.getEmail());

            if (checkEmailOptional.isPresent()) {
                throw new CustomException(ResponseCode.CONFLICT_ACCOUNT);
            }

            commonMember.updateEmail(updateCommonMemberDtoForApp.getEmail());
        }

        if (updateCommonMemberDtoForApp.getChangePassword()) {
            commonMember.updatePassword(passwordEncoder.encode(updateCommonMemberDtoForApp.getPassword()));
        }

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void putChild(Long parentId, Long childId, UpdateCommonMemberDtoForApp updateCommonMemberDtoForApp, MultipartFile file) throws Exception {

        CommonMember commonMember = checkMyChild(parentId, childId);

        commonMember.updateToAddress(
                updateCommonMemberDtoForApp.getName(),
                updateCommonMemberDtoForApp.getBirth(),
                updateCommonMemberDtoForApp.getGender(),
                updateCommonMemberDtoForApp.getGender() == 1 ? "남성" : "여성",
                updateCommonMemberDtoForApp.getNickName(),
                updateCommonMemberDtoForApp.getIntro(),
                updateCommonMemberDtoForApp.getAddress(),
                updateCommonMemberDtoForApp.getAddressDetail(),
                locationService.getLocationCode(updateCommonMemberDtoForApp.getAddress())
        );

        if (updateCommonMemberDtoForApp.getDeleteFileFlag()) {
            commonMember.deleteFile();
        }

        if (file != null) {
            String serverFileName = fileManagement.createServerFileName(file);
            String fileUrl = fileManagement.save(file, serverFileName);
            commonMember.setFile(file.getOriginalFilename(), serverFileName, fileUrl);
        }

        if (updateCommonMemberDtoForApp.getChangeEmail()) {
            if (commonMember.getJoinType() != JoinType.NORMAL) {
                throw new CustomException(ResponseCode.BAD_REQUEST);
            }

            Optional<CommonMember> checkEmailOptional = commonMemberRepository.findByEmail(updateCommonMemberDtoForApp.getEmail());

            if (checkEmailOptional.isPresent()) {
                throw new CustomException(ResponseCode.CONFLICT_ACCOUNT);
            }

            commonMember.updateEmail(updateCommonMemberDtoForApp.getEmail());
        }

        if (updateCommonMemberDtoForApp.getChangePassword()) {
            commonMember.updatePassword(passwordEncoder.encode(updateCommonMemberDtoForApp.getPassword()));
        }

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void putChildSchoolInfo(Long parentId, Long childId, UpdateSchoolInfoDto updateSchoolInfoDto) throws Exception {

        CommonMember commonMember = checkMyChild(parentId, childId);

        commonMember.updateSchoolInfo(
                updateSchoolInfoDto.getSchoolCode(),
                updateSchoolInfoDto.getSchoolName(),
                updateSchoolInfoDto.getGrade(),
                updateSchoolInfoDto.getSchoolClass(),
                updateSchoolInfoDto.getClassNumber()
        );

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void putActivation(Long commonMemberId, UpdateActivationDto updateActivationDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        commonMember.updateActivation(updateActivationDto.getActivation());

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void putSettingAlarm(Long commonMemberId, UpdateSettingAlarmDto updateSettingAlarmDto) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();
        commonMember.updateSettingAlarm(
                updateSettingAlarmDto.getAgreeToService(),
                updateSettingAlarmDto.getAgreeToAd(),
                updateSettingAlarmDto.getAgreeToSchedule()
        );

        commonMemberRepository.save(commonMember);
    }

    @Transactional
    public void delete(Long commonMemberId) throws Exception {

        Optional<CommonMember> commonMemberOptional = commonMemberRepository.findById(commonMemberId);

        if (commonMemberOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        CommonMember commonMember = commonMemberOptional.get();

        if (commonMember.getServerFileName() != null || !commonMember.getServerFileName().isEmpty()) {
            fileManagement.delete(commonMember.getServerFileName());
        }

        if (commonMember.getMemberType() == MemberType.PARENT) {
            commonMemberRepository.deleteByParentId(commonMemberId);
        }

        commonMemberRepository.deleteById(commonMemberId);
    }

    @Transactional
    public void deleteChild(Long parentId, Long childId) throws Exception {

        CommonMember commonMember = checkMyChild(parentId, childId);

        if (commonMember.getServerFileName() != null || !commonMember.getServerFileName().isEmpty()) {
            fileManagement.delete(commonMember.getServerFileName());
        }

        commonMemberRepository.deleteById(commonMember.getId());
    }

    private CommonMember checkMyChild(Long parentId, Long childId) throws Exception {
        Optional<CommonMember> childOptional = commonMemberRepository.findById(childId);

        if (childOptional.isEmpty() || (!childOptional.get().getParentId().equals(parentId) && !childId.equals(parentId))) {

            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        return childOptional.get();
    }

    private String generateUniqueReferralCode() {
        String referralCode;
        boolean isDuplicate;

        // 중복되지 않은 코드를 찾을 때까지 반복
        do {
            // 추천 코드 생성
            referralCode = generateReferralCode();

            // DB에서 해당 코드가 존재하는지 확인
            isDuplicate = commonMemberRepository.findByMyReferralCode(referralCode).isPresent();
        } while (isDuplicate); // 중복이 있으면 다시 생성

        return referralCode;
    }

    private String generateReferralCode() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "").substring(0, 8).toUpperCase();
    }
}
