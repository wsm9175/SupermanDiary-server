package com.lodong.spring.supermandiary.enumvalue;

public enum ConstructorAlarmEnum {
    NEW_ESTIMATE("신규 계약서 요청건"),
    DEFER_ESTIMATE("계약서 반려"),
    CHATTING("채팅 도착");

    private final String label;
    ConstructorAlarmEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
