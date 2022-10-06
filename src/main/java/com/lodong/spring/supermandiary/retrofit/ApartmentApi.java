package com.lodong.spring.supermandiary.retrofit;

import com.lodong.spring.supermandiary.dto.externalapi.ApartmentListResponseDTO;
import feign.Param;
import feign.RequestLine;

public interface ApartmentApi {
    @RequestLine("GET /getTotalAptList" +
                    "?serviceKey={serviceKey}" +
                    "&numOfRows={numOfRows}")
    ApartmentListResponseDTO getApartmentList(@Param(value = "serviceKey", encoded = true) String serviceKey,
                                              @Param(value = "numOfRows") int numOfRows);

}
