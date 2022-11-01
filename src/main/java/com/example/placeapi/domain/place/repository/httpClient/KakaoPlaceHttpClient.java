package com.example.placeapi.domain.place.repository.httpClient;

import com.example.placeapi.domain.place.vo.KakaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class KakaoPlaceHttpClient implements PlaceHttpClient<KakaoResponse> {

    private final RestTemplate kakaoRestTemplate;

    private final WebClient kakaoWebClient;

    @Value("${kakao.place-api.url}")
    private String apiUrl;

    @Value("${kakao.authorization.token}")
    private String token;

    @Override
    public ResponseEntity<KakaoResponse> findPlaceByKeyword(String encodeKeyword, int page, int size) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", String.format("KakaoAK %s", token));

        return kakaoRestTemplate.exchange(
                URI.create(String.format("%s?query=%s&page=%s&size=%s", apiUrl, encodeKeyword, page, size)),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                KakaoResponse.class
        );
    }

    @Override
    public KakaoResponse asyncFindPlaceByKeyword(String keyword, int page, int size) {
        return kakaoWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("query", keyword)
                                .queryParam("page", page)
                                .queryParam("size", size)
                                .build()
                )
                .retrieve()
                .bodyToMono(KakaoResponse.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);
    }
}
