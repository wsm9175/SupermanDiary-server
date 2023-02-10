package com.lodong.spring.supermandiary.enumvalue;

public enum WorkDetailStatusEnum {
    MEASURE("실측"),
    ORDER("발주"),
    BillingMessage("청구"),
    PAY("결제");

    private final String label;
    WorkDetailStatusEnum(String label) {
        this.label = label;
    }
    public String label(){
        return label;
    }
}
