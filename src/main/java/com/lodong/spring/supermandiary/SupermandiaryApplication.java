package com.lodong.spring.supermandiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

//@EnableScheduling
@SpringBootApplication
public class SupermandiaryApplication {
	public static void main(String[] args) {
		SpringApplication.run(SupermandiaryApplication.class, args);
	}
}
