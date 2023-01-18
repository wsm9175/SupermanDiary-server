package com.lodong.spring.supermandiary.enumvalue;

public enum EstimateEnum {
    SEND("전송"),
    PROCESSED("처리완료"),
    REJECT("반려");

    private final String label;
    EstimateEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
