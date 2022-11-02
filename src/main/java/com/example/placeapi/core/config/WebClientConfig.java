package com.example.placeapi.core.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.function.Consumer;

@Configuration
public class WebClientConfig {

    @Value("${kakao.place-api.url}")
    private String kakaoApiUrl;

    @Value("${naver.place-api.url}")
    private String naverApiUrl;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @Value("${kakao.authorization.token}")
    private String token;

    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder()
                .baseUrl(kakaoApiUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .responseTimeout(Duration.ofSeconds(1))
                ))
                .defaultHeader("Authorization", String.format("KakaoAK %s", token))
                .build();
    }

    @Bean
    public WebClient naverWebClient() {
        return WebClient.builder()
                .baseUrl(naverApiUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .responseTimeout(Duration.ofSeconds(1))
                ))
                .defaultHeaders(
                        httpHeaders()
                )
                .build();
    }

    private Consumer<HttpHeaders> httpHeaders() {
        return headers -> {
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);
        };
    }
}
