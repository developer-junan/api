package com.example.placeapi.domain.place.service;

import org.jmock.lib.concurrent.Blitzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
class PlaceServiceTest {

    @Autowired
    private PlaceService placeService;

    int actionCount = 1000;
    int threadCount = 50;

    //50개의 thread로 1000번의 action
    Blitzer blitzer = new Blitzer(actionCount, threadCount);

    @Test
    void storeSearchKeyWord() throws InterruptedException {
        String keyword = "abcd";

        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        blitzer.blitz(new Runnable() {
            public void run() {
                map.putIfAbsent(keyword, 0);
                map.merge(keyword, 1, Integer::sum);
            }
        });

        Assertions.assertEquals(map.get(keyword), actionCount);
    }

    @Test
    void searchKeywordsRank() {
        String[] keys = new String[]{"가", "나", "다", "라", "마"};
        Long[] values = new Long[]{5L, 3L, 4L, 1L, 2L};

        placeService.storeSearchKeyWord("가");
        placeService.storeSearchKeyWord("가");
        placeService.storeSearchKeyWord("가");
        placeService.storeSearchKeyWord("가");
        placeService.storeSearchKeyWord("가");

        placeService.storeSearchKeyWord("나");
        placeService.storeSearchKeyWord("나");
        placeService.storeSearchKeyWord("나");

        placeService.storeSearchKeyWord("다");
        placeService.storeSearchKeyWord("다");
        placeService.storeSearchKeyWord("다");
        placeService.storeSearchKeyWord("다");

        placeService.storeSearchKeyWord("라");

        placeService.storeSearchKeyWord("마");
        placeService.storeSearchKeyWord("마");

        int size = 5;
        Map<String, Long> map = placeService.searchKeywordsRank(size);

        Assertions.assertEquals(map.keySet().size(), size);

        Arrays.sort(values, Collections.reverseOrder());

        int i = 0;

        for (String key : map.keySet()) {
            long value = map.get(key);
            Assertions.assertEquals(values[i], value);
            i++;
        }
    }
}