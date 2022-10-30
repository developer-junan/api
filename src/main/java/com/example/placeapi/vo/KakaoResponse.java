package com.example.placeapi.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KakaoResponse {

    private Meta meta;
    private List<Document> documents;

    @Getter
    public static class Meta {

        @JsonProperty("same_name")
        private SameName sameName;

        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("is_end")
        private boolean isEnd;

        @Getter
        public static class SameName {
            private String[] region;
            private String keyword;

            @JsonProperty("selected_region")
            private String selectedRegion;
        }
    }

    @Getter
    public static class Document {
        private String id;

        @JsonProperty("place_name")
        private String placeName;

        @JsonProperty("category_name")
        private String categoryName;

        @JsonProperty("category_group_code")
        private String categoryGroupCode;

        @JsonProperty("category_group_name")
        private String categoryGroupName;

        private String phone;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("road_address_name")
        private String roadAddressName;

        private String x;

        private String y;

        @JsonProperty("place_url")
        private String placeUrl;

        private String distance;

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", id, placeName, categoryName, categoryGroupCode, categoryGroupName, phone, addressName, roadAddressName, x, y, placeUrl, distance);
        }
    }
}