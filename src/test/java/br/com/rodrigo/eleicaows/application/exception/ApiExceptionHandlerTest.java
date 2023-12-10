package br.com.rodrigo.eleicaows.application.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import br.com.rodrigo.eleicaows.application.model.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

class ApiExceptionHandlerTest {

    @InjectMocks
    private ApiExceptionHandler apiExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleApiException_ShouldReturnErrorResponse() {
        ApiException apiException = new ApiException("Test error", "TEST_CODE", "error", HttpStatus.INTERNAL_SERVER_ERROR.value());

        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleApiException(apiException, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Internal Server Error", ((ErrorResponse) responseEntity.getBody()).getCode());
        assertEquals("TEST_CODE", ((ErrorResponse) responseEntity.getBody()).getDescription());
        assertEquals("error", ((ErrorResponse) responseEntity.getBody()).getMessage());
    }

    @Test
    void handleConstraintViolationException_ShouldReturnErrorResponse() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("fakePath");
        
        when(violation.getPropertyPath()).thenReturn(path);
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException(violations);

        ResponseEntity<Object> responseEntity = apiExceptionHandler.handleConstraintViolationException(constraintViolationException, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Bad Request", ((ErrorResponse) responseEntity.getBody()).getCode());
        assertTrue( ((ErrorResponse) responseEntity.getBody()).getDescription().contains("Erro no campo"));
        assertTrue( ((ErrorResponse) responseEntity.getBody()).getMessage().contains("Contacte o administrador com o código"));
    }
}
