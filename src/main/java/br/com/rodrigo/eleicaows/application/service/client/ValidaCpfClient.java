package br.com.rodrigo.eleicaows.application.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cpfClient", url = "${application.feign.urlCpf}")
public interface ValidaCpfClient {

	@GetMapping(path = "validarCpf/{cpf}")
	Boolean validarCpf(@PathVariable("cpf") String cpf);

}
