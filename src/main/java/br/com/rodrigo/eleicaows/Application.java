package br.com.rodrigo.eleicaows;

import java.util.Date;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableFeignClients
@EnableScheduling
@Slf4j
public class Application {

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
        log.info("Aplicação Spring rodando com TimeZone GMT-3 (São Paulo) :"+new Date());
    }
    
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
