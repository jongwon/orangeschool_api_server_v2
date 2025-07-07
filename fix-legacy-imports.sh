#!/bin/bash

# Community domain
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.follow.entity/com.orangeschool.community.follow.entity/g' domain-modules/community-domain/src/main/java/com/orangeschool/community/follow/repository/FollowRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.cheering.entity/com.orangeschool.community.cheering.entity/g' domain-modules/community-domain/src/main/java/com/orangeschool/community/cheering/repository/CheeringRepositoryImpl.java

# Schedule domain
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.schedule.entity/com.orangeschool.schedule.schedule.entity/g' domain-modules/schedule-domain/src/main/java/com/orangeschool/schedule/schedule/repository/ScheduleRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.calendar.entity/com.orangeschool.schedule.calendar.entity/g' domain-modules/schedule-domain/src/main/java/com/orangeschool/schedule/calendar/repository/CalendarRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.scheduleAlarm.entity/com.orangeschool.schedule.scheduleAlarm.entity/g' domain-modules/schedule-domain/src/main/java/com/orangeschool/schedule/scheduleAlarm/repository/ScheduleAlarmRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.scheduleTemp.entity/com.orangeschool.schedule.scheduleTemp.entity/g' domain-modules/schedule-domain/src/main/java/com/orangeschool/schedule/scheduleTemp/repository/ScheduleTempRepositoryImpl.java

# Support domain
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.suggest.entity/com.orangeschool.support.suggest.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/suggest/repository/SuggestRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.popup.entity/com.orangeschool.support.popup.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/popup/repository/PopupRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.location.entity/com.orangeschool.support.location.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/location/repository/LocationRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.terms.entity/com.orangeschool.support.terms.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/terms/repository/TermsRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.memberAlarm.entity/com.orangeschool.support.memberAlarm.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/memberAlarm/repository/MemberAlarmRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.notice.entity/com.orangeschool.support.notice.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/notice/repository/NoticeRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.manager.entity/com.orangeschool.support.manager.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/manager/repository/ManagerRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.memberNotice.entity/com.orangeschool.support.memberNotice.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/memberNotice/repository/MemberNoticeRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.visitor.entity/com.orangeschool.support.visitor.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/visitor/repository/VisitorRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.report.entity/com.orangeschool.support.report.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/report/repository/ReportRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.alarm.entity/com.orangeschool.support.alarm.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/alarm/repository/AlarmRepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.banner.entity/com.orangeschool.support.banner.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/banner/repository/BannerV2RepositoryImpl.java
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.banner.entity/com.orangeschool.support.banner.entity/g' domain-modules/support-domain/src/main/java/com/orangeschool/support/banner/repository/BannerRepositoryImpl.java

# Education domain
sed -i '' 's/com.orangeschool.orangeschoolapiserver.domain.academy.entity/com.orangeschool.education.academy.entity/g' domain-modules/education-domain/src/main/java/com/orangeschool/education/academy/repository/AcademyRepositoryImpl.java

echo "Legacy imports fixed!"