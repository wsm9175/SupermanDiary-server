package com.lodong.spring.supermandiary.enumvalue;

public enum ChatMessageTypeEnum {
    MESSAGE("MESSAGE"),
    IMAGE("IMAGE");
    private final String label;
    ChatMessageTypeEnum(String label) {
        this.label = label;
    }

    public String label(){
        return label;
    }
}
