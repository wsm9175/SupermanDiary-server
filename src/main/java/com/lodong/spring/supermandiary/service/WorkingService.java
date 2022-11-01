package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.Working;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.dto.WorkApartmentDto;
import com.lodong.spring.supermandiary.repo.WorkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkingService {
    private final WorkingRepository workingRepository;

    public HashMap<String, List<WorkApartmentDto>> getWorkList(String constructorId, int siggCode) {
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();
        List<Working> workingList = workingRepository
                .findByConstructor(constructor)
                .orElseThrow(() -> new NullPointerException("working list가 존재하지 않음"));


        HashMap<String, List<WorkApartmentDto>> workList = new HashMap<>();
        for (Working working : workingList) {
            if (working.getApartment() != null) {
                if (working.getApartment().getSiggCode() == siggCode) {
                    String key = working.getApartment().getName();
                    WorkApartmentDto workApartmentDto = new WorkApartmentDto();
                    workApartmentDto.setApartment(working.getApartment().getName());
                    workApartmentDto.setHosu(working.getApartmentHosu());
                    workApartmentDto.setDong(working.getApartmentDong());
                    workApartmentDto.setWorkId(working.getId());

                    if (workList.get(working.getApartment().getName()) == null) {
                        List<WorkApartmentDto> list = new ArrayList<>();
                        list.add(workApartmentDto);
                        workList.put(key, list);
                    } else {
                        workList.get(key).add(workApartmentDto);
                    }

                }
            } else if (working.getOtherHome() != null) {
                if (working.getOtherHome().getSiggAreas().getCode() == siggCode) {
                    String key = working.getOtherHome().getName();
                    WorkApartmentDto workApartmentDto = new WorkApartmentDto();
                    workApartmentDto.setApartment(working.getOtherHome().getName());
                    workApartmentDto.setHosu(working.getOtherHomeHosu());
                    workApartmentDto.setDong(working.getOtherHomeDong());
                    workApartmentDto.setWorkId(working.getId());

                    if (workList.get(working.getOtherHome().getName()) == null) {
                        List<WorkApartmentDto> list = new ArrayList<>();
                        list.add(workApartmentDto);
                        workList.put(key, list);
                    } else {
                        workList.get(key).add(workApartmentDto);
                    }
                }
            }
        }
        return workList;
    }

    public List<WorkApartmentDto> getWorkByPhoneNumber(String constructorId, String phoneNumber) {
        //회원
        List<Working> memberWorkingList = workingRepository
                .findByConstructorIdAndNonMemberPhoneNumber(constructorId, phoneNumber)
                .orElse(new ArrayList<>());
        //비회원
        List<Working> noneMemberWokringList = workingRepository
                .findByConstructorIdAndNonMemberPhoneNumber(constructorId, phoneNumber)
                .orElse(new ArrayList<>());

        List<Working> joined = new ArrayList<>();
        joined.addAll(memberWorkingList);
        joined.addAll(noneMemberWokringList);

        List<WorkApartmentDto> workApartmentDtos = new ArrayList<>();

        for (Working working : joined) {
            WorkApartmentDto workApartmentDto = new WorkApartmentDto();
            workApartmentDto.setWorkId(working.getId());
            if (working.getApartment() != null) {
                workApartmentDto.setApartment(working.getApartment().getName());
                workApartmentDto.setDong(working.getApartmentDong());
                workApartmentDto.setHosu(working.getApartmentHosu());
            } else if (working.getOtherHome() != null) {
                workApartmentDto.setApartment(working.getOtherHome().getName());
                workApartmentDto.setDong(working.getOtherHomeDong());
                workApartmentDto.setHosu(working.getApartmentHosu());
            }
            workApartmentDtos.add(workApartmentDto);
        }

        return workApartmentDtos;
    }
}

