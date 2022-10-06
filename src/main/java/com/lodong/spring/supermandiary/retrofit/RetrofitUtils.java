package com.lodong.spring.supermandiary.retrofit;

import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@Component
public class RetrofitUtils {
    public <T> T execute(Call<T> call) {
        try {
            Response<T> response = call.execute();

            if (response.isSuccessful()) {
                return response.body();
            } else { //서버 통신은 성공, 하지만 2xx가 아닌 http status가 반환되었다.
                throw new RuntimeException(response.raw().toString());
            }
        } catch (IOException e) { // 서버 통신 실패
            throw new RuntimeException(e.getMessage());
        }
    }
}
