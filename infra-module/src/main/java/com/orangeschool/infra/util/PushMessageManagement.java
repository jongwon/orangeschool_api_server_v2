package com.orangeschool.infra.util;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
// 도메인 엔티티에 대한 직접 의존성 제거
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PushMessageManagement {

    private static final Logger logger = LoggerFactory.getLogger(PushMessageManagement.class);

    public void send(String token, Notification notification) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            logger.error("push error", e);
        }
    }

    @Async
    public void sendToList(List<String> tokens, Notification notification) {
        for (String token : tokens) {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (Exception e) {
                logger.error("push error", e);
            }
        }
    }

    @Async
    public void sendToSchedule(String scheduleTitle, LocalTime startTime, LocalTime endTime, String pushToken, String name) {

        Notification notification = Notification.builder()
                .setTitle(scheduleTitle)
                .setBody(getMessage(scheduleTitle, name, startTime, endTime))
                .build();

        Message message = Message.builder()
                .setToken(pushToken)
                .setNotification(notification)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            logger.error("push error", e);
        }
    }

    private String getMessage(String title, String name, LocalTime startTime, LocalTime endTime) {
        String message ="";
        if(startTime != null){
            message = title + " " + name + " 오늘" + getHourString(startTime);
        }else {
            message = title + " " + name;
        }


        return message;
    }


    private String getHourString(LocalTime localTime) {
        // 출력 형식을 설정할 DateTimeFormatter를 생성합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a hh:mm");

        // 포맷팅된 문자열로 시간을 표시합니다.
        String formattedTime = localTime.format(formatter);

        return formattedTime;
    }
}
