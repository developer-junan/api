package com.example.placeapi.repository.httpClient;

import org.springframework.http.ResponseEntity;

public interface PlaceHttpClient<T> {
    ResponseEntity<T> findPlaceByKeyword(String keyword, int page, int size);
}