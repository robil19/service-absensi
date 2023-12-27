package com.intelsysdata.absensi;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.sl.usermodel.ObjectMetaData.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import com.intelsysdata.absensi.model.Mail;
import com.intelsysdata.absensi.service.EmailService;

@SpringBootApplication
public class AbsensiApplication implements ApplicationRunner {
	private static Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private EmailService emailService;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AbsensiApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		log.info("Sending Email with Thymeleaf HTML Template Example");

		Mail mail = new Mail();
		mail.setFrom("no-reply@intelsysdata.com");
		mail.setTo("robilputra19@gmail.com");
		mail.setSubject("Sending Email with Thymeleaf HTML Template Example");

		ClassPathResource logoImageResource = new ClassPathResource("memorynotfound-logo.png");
		String logoBase64 = Base64.getEncoder().encodeToString(logoImageResource.getInputStream().readAllBytes());

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("logo", logoBase64);
		model.put("name", "Memorynotfound.com");
		model.put("location", "Belgium");
		model.put("signature", "http://memorynotfound.com");
		mail.setModel(model);

		emailService.sendSimpleMessage(mail);
	}
}
