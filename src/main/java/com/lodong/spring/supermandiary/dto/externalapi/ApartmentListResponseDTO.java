package com.lodong.spring.supermandiary.dto.externalapi;

import com.tickaroo.tikxml.annotation.Element;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class ApartmentListResponseDTO {
    private Header header;
    private Body body;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header{
        private String resultCode;
        private String resultMsg;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "body")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Body{
        @XmlElementWrapper(name = "items")
        @XmlElement(name="item")
        private List<Item> items = new ArrayList<>();
        private String numOfRows;
        private String pageNo;
        private String totalCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @XmlRootElement(name = "item")
    public static class Item {
        private String kaptCode;
        private String kaptName;
        private String as1;
        private String as2;
        private String as3;
        private long bjdCode;

    }
}
