package com.example.placeapi.domain.place.vo;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    @Getter
    public static class Item {
        private String title;
        private String link;
        private String category;
        private String description;
        private String telephone;
        private String address;
        private String roadAddress;
        private int mapx;
        private int mapy;

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%d,%d", title, link, category, description, telephone, address, roadAddress, mapx, mapy);
        }
    }

    @Override
    public String toString() {
        return String.format("%s,%d,%d,%d", lastBuildDate, total, start, display);
    }
}