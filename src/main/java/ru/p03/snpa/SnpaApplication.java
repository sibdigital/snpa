package ru.p03.snpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class SnpaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SnpaApplication.class, args);
	}

	@PostConstruct
	void setUTCTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Irkutsk"));
		System.out.println("Date in UTC: " + new Date().toString());
	}

}
