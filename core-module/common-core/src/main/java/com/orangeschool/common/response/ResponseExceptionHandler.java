package com.orangeschool.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected final ResponseEntity handleServerException(Exception e) {

        e.printStackTrace();

        ResponseDto responseDto = ResponseDto.builder()
                .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                .data("")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }

    @ExceptionHandler(CustomException.class)
    protected final ResponseEntity handleCustomException(CustomException e) {

        e.printStackTrace();

        ResponseDto responseDto = ResponseDto.builder()
                .message(e.getResponseCode().getMessage())
                .data("")
                .build();

        return ResponseEntity.status(e.getResponseCode().getStatus()).body(responseDto);
    }
}
