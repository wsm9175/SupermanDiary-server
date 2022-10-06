package com.lodong.spring.supermandiary;

import com.lodong.spring.supermandiary.domain.address.SidoAreas;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.dto.externalapi.ApartmentListResponseDTO;
import com.lodong.spring.supermandiary.retrofit.ApartmentListCallerImpl;
import com.lodong.spring.supermandiary.service.address.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SupermandiaryApplicationTests {
	@Autowired
	private AddressService addressService;

/*	@Autowired
	SupermandiaryApplicationTests(ApartmentListCallerImpl apartmentListCaller) {
		this.apartmentListCaller = apartmentListCaller;


	@Test
	void contextLoads() {
	}

	@Test
	void 조인테스트(){
		for(SiggAreas siggAreas:addressService.getAllArea()){
			SidoAreas sidoAreas = siggAreas.getSidoAreas();
			String sidoName = sidoAreas.getName();
			System.out.println("sidoName = " + sidoName);
		}
	}*/

}
