package com.example.placeapi.repository.httpClient;

import com.example.placeapi.vo.NaverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class NaverPlaceHttpClient implements PlaceHttpClient<NaverResponse> {

    private final RestTemplate naverRestTemplate;

    @Value("${naver.place-api.url}")
    private String apiUrl;

    @Override
    public ResponseEntity<NaverResponse> findPlaceByKeyword(String encodeKeyword, int page, int size) {

        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Naver-Client-Id", "T2xNh26GTwFoPe0bid9h");
        headers.set("X-Naver-Client-Secret", "0SAwDmVHPa");

        return naverRestTemplate.exchange(
                URI.create(String.format("%s?query=%s&display=%d&start=%s&sort=random", apiUrl, encodeKeyword, size, page)),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                NaverResponse.class);
    }
}
