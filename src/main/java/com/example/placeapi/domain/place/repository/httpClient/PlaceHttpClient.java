package com.example.placeapi.domain.place.repository.httpClient;

import org.springframework.http.ResponseEntity;

public interface PlaceHttpClient<T> {
    ResponseEntity<T> findPlaceByKeyword(String keyword, int page, int size);
}