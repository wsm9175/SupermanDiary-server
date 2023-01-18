package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.dayoff.DayOfInfoDto;
import com.lodong.spring.supermandiary.domain.dayoff.DayOfWorkerDto;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructorHoliday;
import com.lodong.spring.supermandiary.dto.calendar.WorkerFilterDto;
import com.lodong.spring.supermandiary.repo.AffiliatedInfoRepository;
import com.lodong.spring.supermandiary.repo.UserConstructorHolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

public class DayOffService {
    private final UserConstructorHolidayRepository userConstructorHolidayRepository;
    private final AffiliatedInfoRepository affiliatedInfoRepository;

    @Transactional(readOnly = true)
    public DayOfInfoDto getWorkerList(String constructorId) {
        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository.findByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 작업 가능한 사람이 없습니다."));

        List<WorkerFilterDto> workerFilterDtos = new ArrayList<>();
        List<DayOfWorkerDto> dayOfWorkerDtos = new ArrayList<>();

        affiliatedInfos.forEach(affiliatedInfo -> {
            if (affiliatedInfo.getUserConstructor().isActive()) {
                WorkerFilterDto workerFilterDto = new WorkerFilterDto(affiliatedInfo.getUserConstructor().getId(),affiliatedInfo.getUserConstructor().getName());
                workerFilterDtos.add(workerFilterDto);
                affiliatedInfo.getUserConstructor().getUserConstructorHolidayList().forEach(userConstructorHoliday -> {
                    DayOfWorkerDto dayOfWorkerDto = new DayOfWorkerDto(userConstructorHoliday.getUserConstructor().getId(), affiliatedInfo.getUserConstructor().getName(), userConstructorHoliday.getDate());
                    dayOfWorkerDtos.add(dayOfWorkerDto);
                });
            }
        });
        DayOfInfoDto dayOfInfoDto = new DayOfInfoDto(workerFilterDtos, dayOfWorkerDtos);
        return dayOfInfoDto;
    }

    @Transactional
    public void addDayOff(String workerId, LocalDate date) {
        UserConstructor userConstructor = UserConstructor.builder()
                .id(workerId)
                .build();

        UserConstructorHoliday userConstructorHoliday = UserConstructorHoliday
                .builder()
                .id(UUID.randomUUID().toString())
                .userConstructor(userConstructor)
                .date(date)
                .build();

        userConstructorHolidayRepository.save(userConstructorHoliday);
    }

    @Transactional
    public void deleteDayOff(String workerId, LocalDate date){
        userConstructorHolidayRepository.deleteByUserConstructorIdAndDate(workerId, date);
    }
}
