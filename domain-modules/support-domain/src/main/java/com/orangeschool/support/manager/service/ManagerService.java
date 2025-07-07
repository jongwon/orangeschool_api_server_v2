package com.orangeschool.support.manager;

import com.orangeschool.common.dto.request.IdListDto;
import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.dto.request.LoginDto;
import com.orangeschool.common.dto.request.UpdateActivationDto;
import com.orangeschool.common.dto.response.AuthDto;
import com.orangeschool.common.enums.ManagerAuthority;
import com.orangeschool.common.response.CustomException;
import com.orangeschool.common.response.ResponseCode;
import com.orangeschool.auth.util.JwtTokenProvider;
import com.orangeschool.support.manager.dto.CheckPhoneDto;
import com.orangeschool.support.manager.dto.CreateManagerDto;
import com.orangeschool.support.manager.dto.ManagerDto;
import com.orangeschool.support.manager.dto.UpdateManagerDto;
import com.orangeschool.support.manager.entity.Manager;
import com.orangeschool.support.manager.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public AuthDto login(LoginDto loginDto) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findByEmail(loginDto.getAccount());

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginDto.getPassword(), managerOptional.get().getPassword())) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_ACCOUNT);
        }

        if (!managerOptional.get().getIsActive()) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_DORMANCY);
        }

        if (!managerOptional.get().getIsApproved()) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_APPROVE);
        }

        AuthDto authDto = new AuthDto();
//        authDto.setId(managerOptional.get().getId());
//        authDto.setAccessToken(jwtTokenProvider.createAccessToken(managerOptional.get().getId(), "ROLE_ADMIN"));

        return authDto;
    }

    @Transactional(readOnly = true)
    public AuthDto checkPhone(CheckPhoneDto checkPhoneDto) throws Exception {

        // Redis에서 인증번호 가져오기
        Object authCode = redisTemplate.opsForValue().get(checkPhoneDto.getAuthNumber());

        if (authCode == null) {
            throw new CustomException(ResponseCode.BAD_REQUEST_AUTH_NUMBER);
        }

        // redis에서 삭제
        redisTemplate.delete(checkPhoneDto.getAuthNumber());

        // 확인 될 경우 accessToken 전달
        Optional<Manager> managerOptional = managerRepository.findByEmail(checkPhoneDto.getAccount());

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        AuthDto authDto = new AuthDto();
        authDto.setId(managerOptional.get().getId());
        authDto.setAccessToken(jwtTokenProvider.createAccessToken(managerOptional.get().getId(), "ROLE_ADMIN"));

        return authDto;
    }

    @Transactional
    public void create(CreateManagerDto createManagerDto) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findByEmail(createManagerDto.getEmail());

        if (managerOptional.isPresent()) {
            throw new CustomException(ResponseCode.CONFLICT_ACCOUNT);
        }

        Manager manager = Manager.builder()
                .password(passwordEncoder.encode(createManagerDto.getPassword()))
                .name(createManagerDto.getName())
                .email(createManagerDto.getEmail())
                .managerAuthority(ManagerAuthority.ADMIN)
                .isApproved(false)
                .isActive(true)
                .accessMenu(createManagerDto.getAccessMenu())
                .build();

        managerRepository.save(manager);
    }

    @Transactional(readOnly = true)
    public Page<ManagerDto> get(Pageable pageable, KeywordSearchDto keywordSearchDto) throws Exception {
        return managerRepository.search(pageable, keywordSearchDto);
    }

    @Transactional(readOnly = true)
    public ManagerDto getById(Long managerId) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findById(managerId);

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Manager manager = managerOptional.get();

        ManagerDto managerDto = ManagerDto.create(manager);
        return managerDto;
    }

    @Transactional
    public void put(Long managerId, UpdateManagerDto updateManagerDto) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findById(managerId);

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Manager manager = managerOptional.get();

        if (!manager.getEmail().equals(updateManagerDto.getEmail())) {
            managerOptional = managerRepository.findByEmail(updateManagerDto.getEmail());

            if (managerOptional.isPresent()) {
                throw new CustomException(ResponseCode.CONFLICT_ACCOUNT);
            }
        }
        manager.update(
                updateManagerDto.getName(),
                updateManagerDto.getEmail(),
                updateManagerDto.getAccessMenu(),
                updateManagerDto.getManagerAuthority());

        if (updateManagerDto.getPasswordChangeFlag()) {
            if (updateManagerDto.getPassword() == null || updateManagerDto.getPassword().isEmpty()) {
                throw new CustomException(ResponseCode.BAD_REQUEST);
            }
            manager.updatePassword(passwordEncoder.encode(updateManagerDto.getPassword()));
        }

        managerRepository.save(manager);
    }

    @Transactional
    public void putActivation(Long managerId, UpdateActivationDto updateActivationDto) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findById(managerId);

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Manager manager = managerOptional.get();
        manager.updateActivation(updateActivationDto.getActivation());

        managerRepository.save(manager);
    }

    @Transactional
    public void putApprove(Long managerId) throws Exception {

        Optional<Manager> managerOptional = managerRepository.findById(managerId);

        if (managerOptional.isEmpty()) {
            throw new CustomException(ResponseCode.NOT_FOUND);
        }

        Manager manager = managerOptional.get();
        manager.updateApprove(true);

        managerRepository.save(manager);
    }

    @Transactional
    public void delete(Long managerId) throws Exception {
        managerRepository.deleteById(managerId);
    }

    @Transactional
    public void deleteAll(IdListDto idListDto) throws Exception {
        managerRepository.deleteAllById(idListDto.getIdList());
    }
}
