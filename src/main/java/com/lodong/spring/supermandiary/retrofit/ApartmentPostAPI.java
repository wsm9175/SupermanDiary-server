package com.lodong.spring.supermandiary.retrofit;

import com.lodong.spring.supermandiary.dto.externalapi.ApartmentListResponseDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface ApartmentPostAPI {
    @GET("/1613000/AptListService2/getTotalAptList")
    Call<ApartmentListResponseDTO> getApartmentList(@Query(value = "serviceKey", encoded = true) String serviceKey, @Query(value = "numOfRows", encoded = true)int numOfRows);
}
