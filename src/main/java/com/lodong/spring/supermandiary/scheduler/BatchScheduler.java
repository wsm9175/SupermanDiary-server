package com.lodong.spring.supermandiary.scheduler;

import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.dto.externalapi.ApartmentListResponseDTO;
import com.lodong.spring.supermandiary.repo.ApartmentRepository;
import com.lodong.spring.supermandiary.repo.address.SiggAreasRepository;
import com.lodong.spring.supermandiary.retrofit.ApartmentApi;
import feign.Feign;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;

@Slf4j
@Component
public class BatchScheduler {
    @Value("${SC-OA-01-01.serviceKey}")
    private String apiKey;
    private int numOfRows = 18806;
    private @Autowired ApartmentRepository apartmentRepository;
    private ApartmentApi apartmentApi;

    @PostConstruct
    public void postConstruct(){
        System.out.println("BatchScheduler Started At : {}" +  LocalDateTime.now().toString());

        JAXBContextFactory jaxbContextFactory = new JAXBContextFactory.Builder()
                .withMarshallerJAXBEncoding("UTF-8")
                .build();

        apartmentApi = Feign.builder()
                .encoder(new JAXBEncoder(jaxbContextFactory))
                .decoder(new JAXBDecoder(jaxbContextFactory))
                .target(ApartmentApi.class, "http://apis.data.go.kr/1613000/AptListService2");
    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("BatchScheduler Finished At : {}, "+LocalDateTime.now().toString());
    }

    @Scheduled(fixedDelay = 86400000)
    public void scheduleTask(){
        System.out.println("scheduleTask");
        System.out.println("apiKey : " + apiKey);
        ApartmentListResponseDTO apartmentListResponseDTO = apartmentApi.getApartmentList(apiKey, numOfRows);
        System.out.println("requestCode : " + apartmentListResponseDTO.getHeader().getResultCode());
        System.out.println("size : " + apartmentListResponseDTO.getBody().getItems().size());
        System.out.println("totalCount : " + apartmentListResponseDTO.getBody().getTotalCount());
        for(ApartmentListResponseDTO.Item item:apartmentListResponseDTO.getBody().getItems()){
            System.out.println(item.toString());
            int siggCode = (int) (Math.abs(item.getBjdCode()) / 100000l);
            System.out.println("sigCode :" + siggCode);
            Apartment apartment = Apartment.builder()
                    .id(item.getKaptCode())
                    .siggCode(siggCode)
                    .bjdCode(Math.abs(item.getBjdCode()))
                    .name(item.getKaptName())
                    .build();
            apartmentRepository.save(apartment);
        }
    }


}
