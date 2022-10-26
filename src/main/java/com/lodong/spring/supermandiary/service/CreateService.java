package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import com.lodong.spring.supermandiary.domain.create.RequestOrderProduct;
import com.lodong.spring.supermandiary.dto.create.ChoiceProductDto;
import com.lodong.spring.supermandiary.dto.create.ConstructorProductDto;
import com.lodong.spring.supermandiary.dto.create.RequestOrderDto;
import com.lodong.spring.supermandiary.repo.ConstructorProductRepository;
import com.lodong.spring.supermandiary.repo.RequestOrderProductRepository;
import com.lodong.spring.supermandiary.repo.RequestOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateService {
    private final RequestOrderRepository requestOrderRepository;
    private final RequestOrderProductRepository requestOrderProductRepository;
    private final ConstructorProductRepository constructorProductRepository;

    public List<RequestOrderDto> getRequestOrderList(String token) throws NullPointerException {
        Constructor constructor = Constructor.builder()
                .id(token)
                .build();

        List<RequestOrder> requestOrderList = requestOrderRepository.findByConstructor(constructor).
                orElseThrow(() -> new NullPointerException("전자계약서 요청건이 없습니다."));

        List<RequestOrderDto> requestOrderDtoList = new ArrayList<>();

        for (RequestOrder requestOrder : requestOrderList) {
            RequestOrderDto requestOrderDto = new RequestOrderDto();
            List<ChoiceProductDto> choiceProducts = new ArrayList<>();

            List<RequestOrderProduct> requestOrderProductList =
                    requestOrderProductRepository.findRequestOrderProductByRequestOrder(requestOrder).orElseThrow(() -> new NullPointerException("주문 상품이 없음."));

            for (RequestOrderProduct requestOrderProduct : requestOrderProductList) {
                ChoiceProductDto choiceProduct = new ChoiceProductDto();
                choiceProduct.setId(requestOrderProduct.getConstructorProduct().getId());
                choiceProduct.setName(requestOrderProduct.getConstructorProduct().getName());
                choiceProducts.add(choiceProduct);
            }
            requestOrderDto.setId(requestOrder.getId());
            requestOrderDto.setOrderer(requestOrder.getCustomer().getName());
            requestOrderDto.setPhoneNumber(requestOrder.getCustomer().getPhoneNumber());
            requestOrderDto.setNote(requestOrder.getNote());
            requestOrderDto.setChoiceProducts(choiceProducts);
            if (requestOrder.getApartment() != null) {
                requestOrderDto.setApartmentName(requestOrder.getApartment().getName());
                requestOrderDto.setDong(requestOrder.getDong());
                requestOrderDto.setHosu(requestOrder.getHosu());
                requestOrderDto.setApartmentType(requestOrder.getApartment_type());
            } else {
                requestOrderDto.setOtherHomeName(requestOrder.getOtherHome().getName());
                requestOrderDto.setOtherHomeAddressDetail(requestOrder.getOtherHomeAddressDetail());
            }
            requestOrderDtoList.add(requestOrderDto);
        }
        return requestOrderDtoList;
    }

    public List<ConstructorProductDto> getProductList(String constructorId) throws NullPointerException{
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        List<ConstructorProduct> constructorProducts = constructorProductRepository
                .findConstructorProductByConstructor(constructor)
                .orElseThrow(()->new NullPointerException("소속된 시공사의 등록된 상품정보가 존재하지 않습니다."));

        List<ConstructorProductDto> constructorProductDtos = new ArrayList<>();

        for(ConstructorProduct constructorProduct:constructorProducts){
            ConstructorProductDto constructorProductDto = new ConstructorProductDto();
            constructorProductDto.setId(constructorProduct.getId());
            constructorProductDto.setName(constructorProduct.getName());
            constructorProductDtos.add(constructorProductDto);
        }

        return constructorProductDtos;

    }

}
