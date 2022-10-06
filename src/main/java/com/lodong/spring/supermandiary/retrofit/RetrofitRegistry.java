package com.lodong.spring.supermandiary.retrofit;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitRegistry {
    private final String APARTMENTLISTAPI = "http://apis.data.go.kr";

    private static final HttpLoggingInterceptor loggingInterceptor
            = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    @Bean
    ApartmentPostAPI getAllApartmentList(){
        OkHttpClient client = new OkHttpClient.Builder()
                //서버로 요청하는데 걸리는 시간을 제한 (15초 이내에 서버에 요청이 성공해야한다. (handshake))
                .connectTimeout(15, TimeUnit.SECONDS)
                //서버로 요청이 성공했고, 응답데이터를 받는데 시간을 제한한다. (15초 이내에 응답 데이터를 전달받아야한다)
                .addInterceptor(loggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(APARTMENTLISTAPI)
                .addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(false).build()))
                .client(client)
                .build()
                .create(ApartmentPostAPI.class);
    }

}
