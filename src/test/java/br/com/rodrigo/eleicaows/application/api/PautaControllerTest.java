package br.com.rodrigo.eleicaows.application.api;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import br.com.rodrigo.eleicaows.application.model.request.PautaRequest;
import br.com.rodrigo.eleicaows.application.model.response.PautaResponse;
import br.com.rodrigo.eleicaows.application.service.PautaService;

@ExtendWith(MockitoExtension.class)
class PautaControllerTest {

    @InjectMocks
    private PautaController pautaController;

    @Mock
    private PautaService pautaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSalvarPauta() throws Exception {
        PautaRequest pautaRequest = new PautaRequest("teste", 5L);
        when(pautaService.criarPauta(pautaRequest)).thenReturn("Pauta salva com sucesso");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/pauta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pautaRequest));

        ResultActions resultActions = performRequest(requestBuilder);
        resultActions.andExpect(status().isCreated());

        verify(pautaService, times(1)).criarPauta(pautaRequest);
    }

    @Test
    void testBuscarPauta() throws Exception {
        String nomePauta = "Teste";
        List<PautaResponse> pautaResponses = Collections.singletonList(
                new PautaResponse(1L, "abcd", StatusEnum.ABERTO, LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(30), Collections.emptyList())
        );

        when(pautaService.buscarPauta(nomePauta)).thenReturn(pautaResponses);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/pauta")
                .param("pauta", nomePauta);

        ResultActions resultActions = performRequest(requestBuilder);

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("abcd")));

        verify(pautaService, times(1)).buscarPauta(nomePauta);
    }

    private ResultActions performRequest(RequestBuilder requestBuilder) throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(pautaController).build();
        return mockMvc.perform(requestBuilder);
    }
}
