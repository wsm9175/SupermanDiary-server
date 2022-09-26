package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.BusinessLicense;
import com.lodong.spring.supermandiary.domain.FileList;
import com.lodong.spring.supermandiary.repo.BusinessLicenseRepository;
import com.lodong.spring.supermandiary.repo.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaveFileService {
    private final FileRepository fileRepository;
    private final BusinessLicenseRepository businessLicenseRepository;
    private final String STORAGE_ROOT_PATH = "C:\\Users\\seongminWoo\\Desktop\\outsourcing\\supermandiary\\supermandiary\\src\\main\\resources\\static";
    private final String BUSINESSLICENSE_PATH = "\\";

    public void saveBusinessLicense(FileList businessLicense, String constructorId, MultipartFile file) throws NullPointerException {
        if (businessLicense != null) {
            //저장경로 및 생성시간 설정
            businessLicense.setStorage(STORAGE_ROOT_PATH + BUSINESSLICENSE_PATH + businessLicense.getName());
            businessLicense.setCreateAt(getNowTime());
            String fileId = businessLicense.getId();
            BusinessLicense businessLicenseInfo = BusinessLicense.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorId(constructorId)
                    .fileId(fileId)
                    .build();
            //DB에 정보 저장
            fileRepository.save(businessLicense);
            try {
                saveFile(file, businessLicense.getStorage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //실제 파일 저장
            businessLicenseRepository.save(businessLicenseInfo);
        } else {
            throw new NullPointerException();
        }
    }

    private void saveFile(MultipartFile file, String storage) throws IOException {
        File saveFile = new File(storage);
        file.transferTo(saveFile);
    }

    private String getNowTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String formatedNow = now.format(formatter);
        return formatedNow;
    }
}
