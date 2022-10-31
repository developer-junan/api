package com.example.placeapi.domain.place.controller;

import com.example.placeapi.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/place/v1")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/search/keywords")
    public List<String> searchByKeyWord(@RequestParam String keyword,
                                        @RequestParam(defaultValue = "1", required = false) int page,
                                        @RequestParam(defaultValue = "5", required = false) int size) throws UnsupportedEncodingException {
        return placeService.searchByKeyWord(keyword, page, size);
    }

    @GetMapping("/search/keywords/rank")
    public Map<String, Long> searchKeywordsRank(@RequestParam(defaultValue = "5") int size) {
        return placeService.searchKeywordsRank(size);
    }
}