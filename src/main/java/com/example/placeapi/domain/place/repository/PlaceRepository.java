package com.example.placeapi.domain.place.repository;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PlaceRepository {
    private final ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();

    public void storeSearchKeyWordCount(String keyword) {
        map.putIfAbsent(keyword, 0L);
        map.merge(keyword, 1L, Long::sum);
    }

    public Map<String, Long> searchKeywordsRank(int size) {

        if (ObjectUtils.isEmpty(map)) {
            return Collections.emptyMap();
        }

        List<Map.Entry<String, Long>> lists = new ArrayList<>(map.entrySet());
        lists.sort((obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));

        Map<String, Long> resultMap = new HashMap<>();

        int count = 0;

        for (Map.Entry<String, Long> entry : lists) {
            resultMap.put(entry.getKey(), entry.getValue());
            count++;

            if (count == size) {
                break;
            }
        }

        return resultMap;
    }
}
