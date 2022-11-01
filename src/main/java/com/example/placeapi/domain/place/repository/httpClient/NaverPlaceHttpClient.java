package com.example.placeapi.domain.place.repository.httpClient;

import com.example.placeapi.domain.place.vo.NaverResponse;
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
public class NaverPlaceHttpClient implements PlaceHttpClient<NaverResponse> {

    private final RestTemplate naverRestTemplate;

    private final WebClient naverWebClient;

    @Value("${naver.place-api.url}")
    private String apiUrl;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Override
    public ResponseEntity<NaverResponse> findPlaceByKeyword(String keyword, int page, int size) {

        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        return naverRestTemplate.exchange(
                URI.create(String.format("%s?query=%s&display=%d&start=%s&sort=random", apiUrl, keyword, size, page)),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                NaverResponse.class);
    }

    @Override
    public NaverResponse asyncFindPlaceByKeyword(String keyword, int page, int size) {
        return naverWebClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("query", keyword)
                                .queryParam("start", page)
                                .queryParam("display", size)
                                .build()
                )
                .retrieve()
                .bodyToMono(NaverResponse.class)
                .flux()
                .toStream()
                .findFirst()
                .orElse(null);
    }
}
