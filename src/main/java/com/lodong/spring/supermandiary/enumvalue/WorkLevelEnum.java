package com.lodong.spring.supermandiary.enumvalue;

public enum WorkLevelEnum {
    MEASUREMENT("실측"),
    CONSTRUCT("시공"),
    PAY("결제");

    private final String label;
    WorkLevelEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
