package com.lodong.spring.supermandiary.dto.externalapi;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Data @ToString
public class ApartmentListPostRequestDTO {
    //@Value("#{api['SC-OA-01-01.serviceKey']}")
    @SerializedName("serviceKey")
    public final String serviceKey = "%2BW1RIrm0rSgLke2YbG39XOQTIa2Yv%2FFrkS3Ip2vEUV5Pt%2BcTyS0nAgzQQSKcPeT1aoiIym8u4XOoLp%2F1sajHoQ%3D%3D";
    @SerializedName("numOfRows")
    public int nomOfRows = 18805;

}
