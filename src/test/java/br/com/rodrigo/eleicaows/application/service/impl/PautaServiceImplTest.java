package br.com.rodrigo.eleicaows.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import br.com.rodrigo.eleicaows.application.exception.ApiException;
import br.com.rodrigo.eleicaows.application.model.enums.StatusEnum;
import br.com.rodrigo.eleicaows.application.model.request.PautaRequest;
import br.com.rodrigo.eleicaows.application.model.response.PautaResponse;
import br.com.rodrigo.eleicaows.domain.entity.Pauta;
import br.com.rodrigo.eleicaows.domain.repository.PautaRepository;

@SpringBootTest
@AutoConfigureMockMvc
class PautaServiceImplTest {

    @Autowired
    private PautaServiceImpl pautaService;

    @MockBean
    private PautaRepository pautaRepository;

    @Test
     void testCriarPauta_Ok() throws ApiException {
        PautaRequest pautaRequest = new PautaRequest("Pauta de Teste", 1L);

        when(pautaRepository.save(any(Pauta.class))).thenReturn(new Pauta());
        String resultado = pautaService.criarPauta(pautaRequest);

        assertEquals(resultado,"Pauta salva com sucesso");
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

	@Test
	void testCriarPauta_Fail() throws ApiException {
		PautaRequest pautaRequest = new PautaRequest("Teste", 1L);
		when(pautaRepository.save(any(Pauta.class))).thenThrow(new RuntimeException("erro ao salvar"));
		ApiException apiException = assertThrows(ApiException.class, () -> pautaService.criarPauta(pautaRequest));

		assertEquals(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), apiException.getStatusCode());
	}

    @Test
     void testBuscarPauta_Ok() throws ApiException {
        String nome = "teste 1";
        List<Pauta> pautas = Arrays.asList(
                new Pauta(1L, "teste 1", StatusEnum.ABERTO, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), null),
                new Pauta(2L, "teste 2", StatusEnum.ABERTO, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), null)
        );

        when(pautaRepository.findByNome(nome)).thenReturn(pautas);
        when(pautaRepository.findAll()).thenReturn(pautas);
        List<PautaResponse> resultado = pautaService.buscarPauta(nome);

        assertEquals(pautas.size(), resultado.size());
        verify(pautaRepository, times(1)).findByNome(nome);
    }
    
    @Test
    void testBuscarPauta_NomeVazio_Ok() throws ApiException {
       List<Pauta> pautas = Arrays.asList(
               new Pauta(1L, "teste 1", StatusEnum.ABERTO, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), null),
               new Pauta(2L, "teste 2", StatusEnum.ABERTO, LocalDateTime.now(), LocalDateTime.now().plusMinutes(60), null)
       );

       when(pautaRepository.findByNome(anyString())).thenReturn(pautas);
       when(pautaRepository.findAll()).thenReturn(pautas);
       List<PautaResponse> resultado = pautaService.buscarPauta(null);

       assertEquals(pautas.size(), resultado.size());
   }

    @Test
     void testBuscarPauta_Vazio() throws ApiException {
        String nome = "Teste";
        when(pautaRepository.findByNome(nome)).thenReturn(List.of());
        List<PautaResponse> resultado = pautaService.buscarPauta(nome);
        
        assertTrue(resultado.isEmpty());
        verify(pautaRepository, times(1)).findByNome(nome);
    }

    @Test
     void testBuscarPauta_Fail() {
        String nome = "Pauta de Teste";
        when(pautaRepository.findByNome(nome)).thenThrow(new RuntimeException("Error buscar pauta"));

        assertThrows(ApiException.class, () -> pautaService.buscarPauta(nome));
    }

   
}
