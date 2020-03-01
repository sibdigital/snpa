package ru.p03.snpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.p03.snpa.repository.RegPractice2Repository;
import ru.p03.snpa.word2vec.Word2VecModelInitializer;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
//public class SnpaApplication extends SpringBootServletInitializer {
public class SnpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnpaApplication.class, args);
	}

	@PostConstruct
	void setUTCTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Irkutsk"));
		System.out.println("Date in UTC: " + new Date().toString());
	//	Word2VecModelInitializer.initialize("model.ser", repository);
	}

	@Autowired
	RegPractice2Repository repository;



}
