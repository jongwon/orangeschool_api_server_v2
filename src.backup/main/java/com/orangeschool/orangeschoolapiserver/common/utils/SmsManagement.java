package com.orangeschool.orangeschoolapiserver.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangeschool.orangeschoolapiserver.common.response.CustomException;
import com.orangeschool.orangeschoolapiserver.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SmsManagement {

    private static final Logger logger = LoggerFactory.getLogger(SmsManagement.class);

    public void send(String receiver, String msg) throws Exception {

        final String user_id = "orangeschool";
        final String key = "3kfcdgwfwg9eopaxi2ll32q43oghdto6";
        final String sender = "023343554";
        final String sms_url = "https://apis.aligo.in/send/"; // 전송요청 URL
        final String encodingType = "utf-8";

        List<NameValuePair> params = new ArrayList<>();

        /********************* 인증정보 *********************/
        params.add(new BasicNameValuePair("user_id", user_id));
        params.add(new BasicNameValuePair("key", key));
        params.add(new BasicNameValuePair("sender", sender));

        /******************** 전송정보 ********************/
        params.add(new BasicNameValuePair("receiver", receiver)); // 수신번호
        params.add(new BasicNameValuePair("msg", msg)); // 메세지 내용
//            params.add(new BasicNameValuePair("testmode_yn", "Y")); // Y 인경우 실제문자 전송X , 자동취소(환불) 처리

        /******************** 요청 ********************/
        HttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(sms_url);
        postRequest.setEntity(new UrlEncodedFormEntity(params, encodingType));
        HttpResponse response = client.execute(postRequest);

        /******************** 응답 ********************/
        ResponseHandler<String> handler = new BasicResponseHandler();
        String body = handler.handleResponse(response);
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> bodyMap = objectMapper.readValue(body, typeReference);

        if (bodyMap.get("result_code").toString().compareTo("-201") == 0) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR_SMS_CASH);
        }
    }
}
