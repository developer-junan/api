package com.example.placeapi;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

class PlaceServiceTest {

    private final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    int actionCount = 1000;
    int threadCount = 50;

    //50개의 thread로 1000번의 action
    Blitzer blitzer = new Blitzer(actionCount, threadCount);

    @Test
    void storeSearchKeyWord() throws InterruptedException {
        String keyword = "abcd";

        blitzer.blitz(new Runnable() {
            public void run() {
                map.putIfAbsent(keyword, 0);
                map.merge(keyword, 1, Integer::sum);
            }
        });

        Assertions.assertEquals(map.get(keyword), actionCount);
    }
}


