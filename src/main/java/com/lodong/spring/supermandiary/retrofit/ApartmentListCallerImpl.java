package com.lodong.spring.supermandiary.retrofit;

import com.lodong.spring.supermandiary.dto.externalapi.ApartmentListPostRequestDTO;
import com.lodong.spring.supermandiary.dto.externalapi.ApartmentListResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@Slf4j
@RequiredArgsConstructor
@Component
public class ApartmentListCallerImpl implements ApartmentListCaller{
    private final RetrofitUtils retrofitUtils;
    private final ApartmentPostAPI apartmentPostAPI;


    @Override
    public void getAllApartmentList() {
        ApartmentListPostRequestDTO apartmentListPostRequestDTO = new ApartmentListPostRequestDTO();
        String serviceKey = apartmentListPostRequestDTO.serviceKey;
        int numOfRows = apartmentListPostRequestDTO.nomOfRows;
        System.out.println("serviceKey : " + serviceKey);
        System.out.println("numOfRows : " + numOfRows);
        Call<ApartmentListResponseDTO> call = apartmentPostAPI.getApartmentList(serviceKey, numOfRows);
        retrofitUtils.execute(call);
        /*call.enqueue(new Callback<ApartmentListResponseDTO>() {
            @Override
            public void onResponse(Call<ApartmentListResponseDTO> call, Response<ApartmentListResponseDTO> response) {
                log.info(response.message());
            }

            @Override
            public void onFailure(Call<ApartmentListResponseDTO> call, Throwable t) {
                log.info(t.getMessage());

            }
        });*/
    };

}
