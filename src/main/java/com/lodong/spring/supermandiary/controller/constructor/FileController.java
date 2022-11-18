package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.file.SaveFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/file/constructor")
public class FileController {
    private final SaveFileService saveFileService;

    public FileController(SaveFileService saveFileService) {
        this.saveFileService = saveFileService;
    }


    @GetMapping("/display")
    public ResponseEntity<?> getImage(String fileName) {
        try {
            String storage = saveFileService.getFileStorage(fileName);
            File file = new File(storage);
            ResponseEntity<byte[]> result = null;
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
            return result;
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "해당 파일이 존재하지 않습니다.";
            return getResponseMessage(statusEnum, message);
        } catch (IOException e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "해당 파일이 존재하지 않습니다.";
            return getResponseMessage(statusEnum, message);
        }
    }
}
