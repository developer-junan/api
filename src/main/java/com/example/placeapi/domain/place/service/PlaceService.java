package com.example.placeapi.domain.place.service;

import com.example.placeapi.domain.place.repository.PlaceRepository;
import com.example.placeapi.domain.place.repository.httpClient.KakaoPlaceHttpClient;
import com.example.placeapi.domain.place.repository.httpClient.NaverPlaceHttpClient;
import com.example.placeapi.domain.place.vo.KakaoResponse;
import com.example.placeapi.domain.place.vo.NaverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResourceAccessException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final KakaoPlaceHttpClient kakaoPlaceHttpClient;
    private final NaverPlaceHttpClient naverPlaceHttpClient;
    private final PlaceRepository placeRepository;

    public List<String> searchByKeyWord(String keyword, int page, int size) throws UnsupportedEncodingException {
        storeSearchKeyWord(keyword);

        String encodeKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());

        List<String> placesByKakao = getPlaceListByKakao(encodeKeyword, page, size);
        List<String> placesByNaver = getPlaceListByNaver(encodeKeyword, page, size);

        if (ObjectUtils.isEmpty(placesByKakao) && ObjectUtils.isEmpty(placesByNaver)) {
            return Collections.emptyList();
        }

        Map<String, List<String>> placeAggregationMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(placesByKakao)) {
            placeAggregationMap.put("kakao", placesByKakao);
        }

        if (!ObjectUtils.isEmpty(placesByNaver)) {
            placeAggregationMap.put("naver", placesByNaver);
        }

        //ex.. google 추가 시 api 연동 결과 List, placeAggregationMap에 추가 로직 1줄
        return orderByPlaces(placeAggregationMap, size);
    }

    public List<String> orderByPlaces(Map<String, List<String>> placeAggregationMap, int size) {
        int searchTargetCount = placeAggregationMap.keySet().size();

        if (searchTargetCount == 0) {
            return Collections.emptyList();
        }

        List<String> companies = new ArrayList<>(placeAggregationMap.keySet());

        if (searchTargetCount == 1) {
            return placeAggregationMap.get(companies.get(0));
        }

        Map<String, Float> resultMap = calculateRating(placeAggregationMap, searchTargetCount, companies);

        List<Map.Entry<String, Float>> lists = new ArrayList<>(resultMap.entrySet());
        lists.sort((obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));

        return lists.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(() -> new ArrayList<>(searchTargetCount * size)));
    }

    private Map<String, Float> calculateRating(Map<String, List<String>> placeAggregationMap, int searchTargetCount, List<String> companies) {

        Map<String, Float> resultMap = new HashMap<>();

        for (int i = 0; i < searchTargetCount - 1; i++) {

            List<String> companyAResult = placeAggregationMap.get(companies.get(i));

            for (int j = i + 1; j < searchTargetCount; j++) {

                List<String> companyBResult = placeAggregationMap.get(companies.get(j));

                for (String keyword : companyAResult) {
                    resultMap.putIfAbsent(keyword, 0.0f);

                    if (companies.get(i).equals("kakao")) {
                        resultMap.put(keyword, resultMap.get(keyword) + 0.1f);
                    }

                    resultMap.put(keyword, companyBResult.contains(keyword) ? resultMap.get(keyword) + 2.0f : resultMap.get(keyword) + 1.0f);
                }

                for (String keyword : companyBResult) {
                    resultMap.putIfAbsent(keyword, 1.0f);

                    if (companies.get(j).equals("kakao")) {
                        resultMap.put(keyword, resultMap.get(keyword) + 0.1f);
                    }
                }
            }
        }

        return resultMap;
    }

    private List<String> getPlaceListByKakao(String encodeKeyword, int page, int size) {

        try {
            ResponseEntity<KakaoResponse> response = kakaoPlaceHttpClient.findPlaceByKeyword(encodeKeyword, page, size);

            if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getBody())) {
                return Collections.emptyList();
            }

            List<KakaoResponse.Document> documents = response.getBody().getDocuments();

            if (ObjectUtils.isEmpty(documents)) {
                return Collections.emptyList();
            }

            return documents.stream()
                    .map(document -> document.getPlaceName().trim())
                    .collect(Collectors.toList());

        } catch (ResourceAccessException ex) {
            return Collections.emptyList();
        }
    }

    private List<String> getPlaceListByNaver(String encodeKeyword, int page, int size) {
        try {
            ResponseEntity<NaverResponse> response = naverPlaceHttpClient.findPlaceByKeyword(encodeKeyword, page, size);

            if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getBody())) {
                return Collections.emptyList();
            }

            List<NaverResponse.Item> items = response.getBody().getItems();

            if (ObjectUtils.isEmpty(items)) {
                return Collections.emptyList();
            }

            return items.stream()
                    .map(item -> item.getTitle().replaceAll("<b>", "").replaceAll("</b>", "").trim())
                    .collect(Collectors.toList());

        } catch (ResourceAccessException ex) {
            return Collections.emptyList();
        }
    }


    public void storeSearchKeyWord(String keyword) {
        placeRepository.storeSearchKeyWord(keyword);
    }

    public Map<String, Long> searchKeywordsRank(int size) {
        return placeRepository.searchKeywordsRank(size);
    }
}
