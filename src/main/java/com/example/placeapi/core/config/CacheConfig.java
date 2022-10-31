package com.example.placeapi.core.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

//    @Bean
//    public CacheManager cacheManager(Caffeine caffeine) {
//        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
//        caffeineCacheManager.setCaffeine(caffeine);
//        return caffeineCacheManager;
//    }
}