package com.example.placeapi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<String> searchByKeyWord(String keyword, int page, int size) throws UnsupportedEncodingException {
        return placeService.searchByKeyWord(keyword, page, size);
    }

    @GetMapping("/search/keywords/rank")
    public Map<String, Long> searchKeywordsRank(int size) {
        return placeService.searchKeywordsRank(size);
    }
}