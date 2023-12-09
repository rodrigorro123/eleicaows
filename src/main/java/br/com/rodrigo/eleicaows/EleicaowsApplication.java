package br.com.rodrigo.eleicaows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableFeignClients
public class EleicaowsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EleicaowsApplication.class, args);
	}

}
