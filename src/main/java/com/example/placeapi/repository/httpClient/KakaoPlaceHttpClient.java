package com.example.placeapi.repository.httpClient;

import com.example.placeapi.vo.KakaoResponse;
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
public class KakaoPlaceHttpClient implements PlaceHttpClient<KakaoResponse> {

    private final RestTemplate kakaoRestTemplate;

    @Value("${kakao.place-api.url}")
    private String apiUrl;

    @Override
    public ResponseEntity<KakaoResponse> findPlaceByKeyword(String encodeKeyword, int page, int size) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "KakaoAK 299471cbf59110f557b8a296b70c56f7");

        return kakaoRestTemplate.exchange(
                URI.create(String.format("%s?query=%s&page=%s&size=%s", apiUrl, encodeKeyword, page, size)),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                KakaoResponse.class
        );
    }
}
