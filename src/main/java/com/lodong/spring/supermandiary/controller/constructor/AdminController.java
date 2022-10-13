package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.domain.Constructor;
import com.lodong.spring.supermandiary.domain.ConstructorWorkArea;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.dto.ConstructorWorkAreaDTO;
import com.lodong.spring.supermandiary.service.ApartmentService;
import com.lodong.spring.supermandiary.service.address.AddressService;
import com.lodong.spring.supermandiary.service.address.ConstructorAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("rest/v1/admin/construct")
public class AdminController {
    private final AddressService addressService;
    private final ConstructorAddressService constructorAddressService;

    private final ApartmentService apartmentService;

    public AdminController(AddressService addressService, ConstructorAddressService constructorAddressService, ApartmentService apartmentService) {
        this.addressService = addressService;
        this.constructorAddressService = constructorAddressService;
        this.apartmentService = apartmentService;
    }

    @GetMapping("/get/all-area")
    public List<SiggAreas> getAllArea() {
        log.info("get/all-area");
        List<SiggAreas> siggAreas = addressService.getAllArea();
        return siggAreas;
    }

    @GetMapping("/get/work-area")
    public List<ConstructorWorkArea> getArea(String constructorId) {
        log.info("get/work-area");
        List<ConstructorWorkArea> constructorWorkAreas = constructorAddressService.getWorkAreas(constructorId);
        return constructorWorkAreas;
    }

    @PostMapping("/add/work-area")
    public ResponseEntity<?> addWorkArea(@RequestParam String constructorId, @RequestBody List<ConstructorWorkAreaDTO> constructorWorkAreaDTOS) {
        log.info("/add/work-area");
        List<ConstructorWorkArea> constructorWorkAreas = new ArrayList<>();

        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        for (ConstructorWorkAreaDTO constructorWorkAreaDTO : constructorWorkAreaDTOS) {
            int siggCode = constructorWorkAreaDTO.getSiggCode();
            System.out.println("siggCode : " + siggCode);
            SiggAreas siggAreas = SiggAreas.builder()
                    .code(siggCode)
                    .build();

            ConstructorWorkArea constructorWorkArea = ConstructorWorkArea.builder()
                    .siggAreas(siggAreas)
                    .constructor(constructor)
                    .build();

            System.out.println(constructorWorkArea.toString());
            constructorWorkAreas.add(constructorWorkArea);
        }

        constructorAddressService.addWorkAreas(constructorWorkAreas);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/work-apartment")
    public List<Apartment> getArea(int siggCode) {
        log.info("get/work-apartment");
        List<Apartment> apartmentList = apartmentService.getApartmentBySigg(siggCode);
        return apartmentList;
    }

}
