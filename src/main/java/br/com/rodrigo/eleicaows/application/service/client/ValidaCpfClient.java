package br.com.rodrigo.eleicaows.application.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cpfClient", url = "https://validarcpf-f68273501cba.herokuapp.com")
public interface ValidaCpfClient {
	
	@GetMapping(path = "validarCpf/{cpf}"  )
	Boolean validarCpf(@PathVariable("cpf") String cpf);

}
