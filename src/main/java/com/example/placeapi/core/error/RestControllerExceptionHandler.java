package com.example.placeapi.core.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseVO unknownException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return getErrorResponse(ErrorTypeEnum.ERROR_9999);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseVO missingServletRequestParameterException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return getErrorResponse(ErrorTypeEnum.ERROR_0001);
    }

//    @ExceptionHandler(value = {UnsupportedEncodingException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponseVO unsupportedEncodingException(Exception ex) {
//        log.error(ex.getMessage(), ex);
//        return getErrorResponse(ErrorTypeEnum.ERROR_0002);
//    }


    private ErrorResponseVO getErrorResponse(ErrorTypeEnum errorTypeEnum) {
        return ErrorResponseVO.builder()
                .errorCode(errorTypeEnum.getErrorCode())
                .errorMessage(errorTypeEnum.getErrorMessage())
                .build();
    }
}
