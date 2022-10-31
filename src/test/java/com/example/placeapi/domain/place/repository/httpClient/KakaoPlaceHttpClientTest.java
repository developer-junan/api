package com.example.placeapi.domain.place.repository.httpClient;

import com.example.placeapi.domain.place.vo.KakaoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class KakaoPlaceHttpClientTest {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${kakao.place-api.url}")
    private String apiUrl;

    @Test
    void apiCall() throws UnsupportedEncodingException {

        Exception exception = assertThrows(ResourceAccessException.class, () -> {
            String encodeKeyword = URLEncoder.encode("곱창", StandardCharsets.UTF_8.toString());

            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "KakaoAK 299471cbf59110f557b8a296b70c56f7");

            restTemplate.exchange(
                    URI.create(String.format("%s?query=%s&page=%s&size=%s", apiUrl, encodeKeyword, 1, 5)),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    KakaoResponse.class
            );
        });
    }
}