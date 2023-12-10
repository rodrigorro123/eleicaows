package br.com.rodrigo.eleicaows.application.api;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rodrigo.eleicaows.application.model.enums.VotoEnum;
import br.com.rodrigo.eleicaows.application.model.request.VotoRequest;
import br.com.rodrigo.eleicaows.application.model.response.ResultadoApuracaoResponse;
import br.com.rodrigo.eleicaows.application.service.ApuracaoService;

@ExtendWith(MockitoExtension.class)
class ApuracaoControllerTest {

	@InjectMocks
	private ApuracaoController apuracaoController;

	@Mock
	private ApuracaoService apuracaoService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testSalvarVoto() throws Exception {
		VotoRequest votoRequest = new VotoRequest("abcd", "12345678901", VotoEnum.SIM);
		when(apuracaoService.registrarVoto(votoRequest)).thenReturn("Voto realizado com sucesso");

		RequestBuilder requestBuilder = post("/voto").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(votoRequest));

		ResultActions resultActions = performRequest(requestBuilder);

		resultActions.andExpect(status().isCreated()).andExpect(content().string("Voto realizado com sucesso"));
		verify(apuracaoService, times(1)).registrarVoto(votoRequest);
	}

	@Test
	void testConsultarResultado() throws Exception {
		String nomePauta = "p123";
		ResultadoApuracaoResponse resultadoApuracaoResponse = new ResultadoApuracaoResponse(Collections.emptyMap());
		when(apuracaoService.consultarResultadoApuracao(nomePauta)).thenReturn(resultadoApuracaoResponse);

		RequestBuilder requestBuilder = get("/voto/resultado").param("pauta", nomePauta);

		ResultActions resultActions = performRequest(requestBuilder);

		resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.resultados").isEmpty());

		verify(apuracaoService, times(1)).consultarResultadoApuracao(nomePauta);
	}

	private ResultActions performRequest(RequestBuilder requestBuilder) throws Exception {
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(apuracaoController).build();

		return mockMvc.perform(requestBuilder);
	}
}
