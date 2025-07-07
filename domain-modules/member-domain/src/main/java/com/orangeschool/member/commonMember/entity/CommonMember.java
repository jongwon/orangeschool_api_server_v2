package com.orangeschool.member.commonMember.entity;

import com.orangeschool.common.entity.BaseEntity;
import com.orangeschool.common.enums.JoinType;
import com.orangeschool.common.enums.MemberType;
// TODO: 다른 도메인 모듈들의 엔티티 의존성을 제거해야 함
// import com.orangeschool.education.challenge.entity.Challenge;
// import com.orangeschool.education.challengeTemp.entity.ChallengeTemp;
// import com.orangeschool.community.cheering.entity.Cheering;
// import com.orangeschool.community.follow.entity.Follow;
// import com.orangeschool.education.memberAcademy.entity.MemberAcademy;
// import com.orangeschool.support.memberAlarm.entity.MemberAlarm;
// import com.orangeschool.support.memberNotice.entity.MemberNotice;
// import com.orangeschool.community.pick.entity.PickLike;
// import com.orangeschool.support.report.entity.Report;
// import com.orangeschool.schedule.schedule.entity.DayMessage;
// import com.orangeschool.schedule.schedule.entity.Schedule;
// import com.orangeschool.schedule.schedule.entity.SleepInfo;
// import com.orangeschool.schedule.scheduleTemp.entity.ScheduleTemp;
// import com.orangeschool.education.schoolSchedule.entity.SchoolSchedule;
// import com.orangeschool.community.story.entity.StoryComment;
// import com.orangeschool.community.story.entity.StoryLike;
// import com.orangeschool.community.story.entity.StoryReply;
// import com.orangeschool.community.story.entity.Story;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CommonMember extends BaseEntity {

    private JoinType joinType;
    private MemberType memberType;
    private String email;
    private String name;
    private String birth;
    private Integer gender;
    private String genderTitle;
    private String phoneNumber;
    @Column(columnDefinition = "TEXT")
    private String originFileName;
    @Column(columnDefinition = "TEXT")
    private String serverFileName;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;
    @Column(columnDefinition = "TEXT")
    private String address;
    @Column(columnDefinition = "TEXT")
    private String addressDetail;
    private long locationCode;
    private Boolean agreeToSms;
    private Boolean agreeToService;
    private Boolean agreeToAd;
    private Boolean agreeToSchedule;
    private Boolean isActive;
    @Column(columnDefinition = "TEXT")
    private String pushToken;
    @Column(columnDefinition = "TEXT")
    private String socialToken;
    @Column(columnDefinition = "TEXT")
    private String password;
    @Column(unique = true)
    private String myReferralCode; //나의 추천 코드
    private String referralCode; // 내가 등록한 다른 사람의 추천 코드
    private String referralCodeTemp; // 내가 등록한 다른 사람의 추천 코드 (승인 전)
    // 우리동네 지역태그
    private String regionNameTag; // 예: "#서울 강남구#강원 강릉시#서울 강북구", 시도 시군구 title 배열
    private String regionCodeTag; // 예: "#1168000000#1174000000#1130500000", 시군구 value 배열
    private String parentNickName; // 예: "#1168000000#1174000000#1130500000", 시군구 value 배열


    // 자녀일 경우
    private String nickName;
    @Column(columnDefinition = "TEXT")
    private String intro;
    private String schoolCode;
    private String schoolName;
    private String grade;
    private String schoolClass;
    private String classNumber;
    private Long parentId;
    private String parentName;
    private Boolean challengeProgress;
    private int totalOrange;
    private int totalOrangeAll;
    @Column(columnDefinition = "TEXT")
    private String timetableEdit;

    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<MemberAcademy> memberAcademies = new HashSet<>();
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<Schedule> schedules = new HashSet<>();
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<Challenge> challenges = new HashSet<>();
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<MemberNotice> memberNotices = new HashSet<>();
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<MemberAlarm> memberAlarms = new HashSet<>();
    //내가 추가한 사람들
    // @OneToMany(mappedBy = "followingMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<Follow> following = new HashSet<>();
    //나를 추가한 사람들
    // @OneToMany(mappedBy = "followerMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<Follow> followers = new HashSet<>();
    // 내가 응원하는 사람들
    // @OneToMany(mappedBy = "cheeringMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<Cheering> cheeringMembers = new HashSet<>();
    // 나를 응원하는 사람들
    // @OneToMany(mappedBy = "cheeredMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<Cheering> cheeredMembers = new HashSet<>();
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<DayMessage> dayMessages = new HashSet<>();
    // @OneToOne(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // private SleepInfo sleepInfo;
    // 이야기작성글
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<Story> stories = new HashSet<>();
    // 댓글
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<StoryComment> storyComments = new HashSet<>();
    // 내가 한 답글
    // @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<StoryReply> sendReplies = new HashSet<>();
    // 내가 받은 답글
    // @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<StoryReply> receiveReplies = new HashSet<>();
    // 이야기 좋아요
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<StoryLike> storyLikes = new HashSet<>();
    // 일정 승인요청목록
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<ScheduleTemp> scheduleTemps = new HashSet<>();
    // 챌린지 수정요청목록
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id asc")
    // private Set<ChallengeTemp> challengeTemps = new HashSet<>();
    //내가 신고한 사람들
    // @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<Report> reporter = new HashSet<>();
    //나를 신고한 사람들
    // @OneToMany(mappedBy = "reportedMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<Report> reportedMember = new HashSet<>();
    // 학교 시간표 편집
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<SchoolSchedule> schoolSchedules = new HashSet<>();
    // 매거진 좋아요
    // @OneToMany(mappedBy = "commonMember", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy("id desc")
    // private Set<PickLike> pickLikes = new HashSet<>();

    public void setFile(String originFileName, String serverFileName, String fileUrl) {
        this.originFileName = originFileName;
        this.serverFileName = serverFileName;
        this.fileUrl = fileUrl;
    }

    public void deleteFile() {
        this.serverFileName = "";
        this.originFileName = "";
        this.fileUrl = "";
    }

    public void update(String name, int gender, String genderTitle, String nickName, String intro) {
        this.name = name;
        this.gender = gender;
        this.genderTitle = genderTitle;
        this.nickName = nickName;
        this.intro = intro;
    }

    public void updateToAddress(String name, String birth, int gender, String genderTitle, String nickName, String intro, String address, String addressDetail, long locationCode) {
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.genderTitle = genderTitle;
        this.nickName = nickName;
        this.intro = intro;
        this.address = address;
        this.addressDetail = addressDetail;
        this.locationCode = locationCode;
    }

    public void updateAddress(String address, String addressDetail, long locationCode) {
        this.address = address;
        this.addressDetail = addressDetail;
        this.locationCode = locationCode;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateSchoolInfo(String schoolCode, String schoolName, String grade, String schoolClass, String classNumber) {
        this.schoolCode = schoolCode;
        this.schoolName = schoolName;
        this.grade = grade;
        this.schoolClass = schoolClass;
        this.classNumber = classNumber;
        this.timetableEdit = null;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateActivation(Boolean activation) {
        this.isActive = activation;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateChallengeProgress(Boolean challengeProgress) {
        this.challengeProgress = challengeProgress;
    }

    public void updateTotalOrange(int totalOrange) {
        this.totalOrange = totalOrange;
        this.totalOrangeAll = totalOrange;
    }

    public void updateSettingAlarm(Boolean agreeToService, Boolean agreeToAd, Boolean agreeToSchedule) {
        this.agreeToService = agreeToService;
        this.agreeToAd = agreeToAd;
        this.agreeToSchedule = agreeToSchedule;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public void updateSettingRegion(String regionNameTag, String regionCodeTag) {
        this.regionNameTag = regionNameTag;
        this.regionCodeTag = regionCodeTag;
    }

    public void updateParentNickname(String parentNickName) {
        this.parentNickName = parentNickName;
    }

    public void updateReferralCode(String referralCode, String referralCodeTemp) {
        this.referralCode = referralCode;
        this.referralCodeTemp = referralCodeTemp;
    }

    public void updateMyReferralCode(String myReferralCode) {
        this.myReferralCode = myReferralCode;
    }

    public void updatTimetableEdit(String timetableEdit) {
        this.timetableEdit = timetableEdit;
    }
}
