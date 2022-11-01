package com.example.placeapi.domain.place.service;

import com.example.placeapi.domain.place.repository.PlaceRepository;
import com.example.placeapi.domain.place.repository.httpClient.KakaoPlaceHttpClient;
import com.example.placeapi.domain.place.repository.httpClient.NaverPlaceHttpClient;
import com.example.placeapi.domain.place.vo.KakaoResponse;
import com.example.placeapi.domain.place.vo.NaverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final KakaoPlaceHttpClient kakaoPlaceHttpClient;
    private final NaverPlaceHttpClient naverPlaceHttpClient;
    private final PlaceRepository placeRepository;

    @Cacheable(cacheNames = "search_keyword", key = "#keyword")
    public List<String> searchByKeyWord(String keyword, int page, int size) {
        storeSearchKeyWordCount(keyword);

        KakaoResponse kakaoResponse = kakaoPlaceHttpClient.asyncFindPlaceByKeyword(keyword, page, size);
        List<String> placesByKakao = getPlaceListByKakao(keyword, page, size);
        List<String> placesByNaver = getPlaceListByNaver(keyword, page, size);

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
        return orderBySearchResult(placeAggregationMap, size);
    }

    public List<String> orderBySearchResult(Map<String, List<String>> placeAggregationMap, int size) {
        int searchTargetCount = placeAggregationMap.keySet().size();

        if (searchTargetCount == 0) {
            return Collections.emptyList();
        }

        List<String> companies = new ArrayList<>(placeAggregationMap.keySet());

        if (searchTargetCount == 1) {
            return placeAggregationMap.get(companies.get(0));
        }

        Map<String, Float> resultMap = calculateScore(placeAggregationMap, searchTargetCount, companies);

        List<Map.Entry<String, Float>> lists = new ArrayList<>(resultMap.entrySet());
        lists.sort((obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));

        return lists.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(() -> new ArrayList<>(searchTargetCount * size)));
    }

    private Map<String, Float> calculateScore(Map<String, List<String>> placeAggregationMap, int searchTargetCount, List<String> companies) {

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

    private List<String> getPlaceListByKakao(String keyword, int page, int size) {

        try {
            KakaoResponse kakaoResponse = kakaoPlaceHttpClient.asyncFindPlaceByKeyword(keyword, page, size);

            if (ObjectUtils.isEmpty(kakaoResponse) || ObjectUtils.isEmpty(kakaoResponse.getDocuments())) {
                return Collections.emptyList();
            }

            List<KakaoResponse.Document> documents = kakaoResponse.getDocuments();

            if (ObjectUtils.isEmpty(documents)) {
                return Collections.emptyList();
            }

            return documents.stream()
                    .map(document -> document.getPlaceName().trim())
                    .collect(Collectors.toList());

        } catch (ResourceAccessException | WebClientRequestException ex) {
            log.error(ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    private List<String> getPlaceListByNaver(String keyword, int page, int size) {
        try {
            NaverResponse response = naverPlaceHttpClient.asyncFindPlaceByKeyword(keyword, page, size);

            if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getItems())) {
                return Collections.emptyList();
            }

            List<NaverResponse.Item> items = response.getItems();

            if (ObjectUtils.isEmpty(items)) {
                return Collections.emptyList();
            }

            return items.stream()
                    .map(item -> item.getTitle().replaceAll("<b>", "").replaceAll("</b>", "").trim())
                    .collect(Collectors.toList());

        } catch (ResourceAccessException | WebClientRequestException ex) {
            log.error(ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }


    public void storeSearchKeyWordCount(String keyword) {
        placeRepository.storeSearchKeyWordCount(keyword);
    }

    public Map<String, Long> searchKeywordsRank(int size) {
        return placeRepository.searchKeywordsRank(size);
    }
}
