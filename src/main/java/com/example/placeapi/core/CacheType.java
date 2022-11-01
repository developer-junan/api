package com.example.placeapi.core;

import lombok.Getter;

@Getter
public enum CacheType {

    SEARCH_KEYWORD("search_keyword", 3);

    private final String cacheName;
    private final int expireAfterAccess;

    CacheType(String cacheName, int expireAfterAccess) {
        this.cacheName = cacheName;
        this.expireAfterAccess = expireAfterAccess;
    }
}
