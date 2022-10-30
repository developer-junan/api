package com.example.placeapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;


public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        System.out.println(response.getStatusCode().value());
        if (response.getStatusCode() == HttpStatus.PRECONDITION_FAILED) {
            //throw new CUSTOM_EXCEPTION(msg);
        }
    }
}
