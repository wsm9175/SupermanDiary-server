package com.lodong.spring.supermandiary.enumvalue;

public enum WorkingStatusEnum {
    //현재 작업 단계에 작업자가 배정된 경우
    ASSIGNED("배정됨"),
    //현재 작업 단계에 작업자가 배정되지 않은 경우
    NOTASSIGNED("배정안됨");

    private final String label;
    WorkingStatusEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
