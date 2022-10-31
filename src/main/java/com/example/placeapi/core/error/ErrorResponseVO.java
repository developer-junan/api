package com.example.placeapi.core.error;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseVO {

    private String errorCode;

    private String errorMessage;
}