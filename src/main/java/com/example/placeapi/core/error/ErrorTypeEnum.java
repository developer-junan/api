package com.example.placeapi.core.error;

import lombok.Getter;

@Getter
public enum ErrorTypeEnum {

    ERROR_0001("0001", "필수 파라미터를 입력해 주세요"),
    ERROR_0002("0002", "파라미터 인코딩 에러"),
    ERROR_9999("9999", "알 수 없는 에러, 관리자에게 문의하세요.");
    private final String errorCode;
    private final String errorMessage;

    ErrorTypeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
