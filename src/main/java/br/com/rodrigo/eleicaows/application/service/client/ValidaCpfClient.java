package br.com.rodrigo.eleicaows.application.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import feign.Param;

@FeignClient(name = "cpfClient", url = "https://validarcpf-f68273501cba.herokuapp.com")
public interface ValidaCpfClient {
	
	@GetMapping(value = "validarCpf{cpf}")
	Boolean validarCpf(@Param("cpf") String cpf);

}
