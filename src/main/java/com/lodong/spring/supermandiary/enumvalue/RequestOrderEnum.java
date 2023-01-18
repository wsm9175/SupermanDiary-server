package com.lodong.spring.supermandiary.enumvalue;

public enum RequestOrderEnum {
    BASIC("일반"),
    DEFER("보류"),
    DELETE("삭제"),
    PROCESSING("처리중"),
    PROCESSED("처리완료"),
    REJECT("반려");

    private final String label;
    RequestOrderEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
