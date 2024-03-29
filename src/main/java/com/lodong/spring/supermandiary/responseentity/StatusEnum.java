package com.lodong.spring.supermandiary.responseentity;

public enum StatusEnum {
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NO_PERMISSION(403,"NO_PERMISSION"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");

    int statusCode;
    String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}
