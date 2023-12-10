package br.com.rodrigo.eleicaows.application.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionTest {

	  @Test
	    void createBadRequestException_ShouldSetFieldsCorrectly() {
	        String reason = "Bad Request Reason";
	        String message = "Bad Request Message";
	        Integer statusCode = HttpStatus.BAD_REQUEST.value();

	        ApiException apiException = ApiException.badRequest(reason, message);

	        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createUnauthorizedException_ShouldSetFieldsCorrectly() {
	        String reason = "Unauthorized Reason";
	        String message = "Unauthorized Message";
	        Integer statusCode = HttpStatus.UNAUTHORIZED.value();

	        ApiException apiException = ApiException.unauthorized(reason, message);

	        assertEquals(HttpStatus.UNAUTHORIZED.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createForbiddenException_ShouldSetFieldsCorrectly() {
	        String reason = "Forbidden Reason";
	        String message = "Forbidden Message";
	        Integer statusCode = HttpStatus.FORBIDDEN.value();

	        ApiException apiException = ApiException.forbidden(reason, message);

	        assertEquals(HttpStatus.FORBIDDEN.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createNotFoundException_ShouldSetFieldsCorrectly() {
	        String reason = "Not Found Reason";
	        String message = "Not Found Message";
	        Integer statusCode = HttpStatus.NOT_FOUND.value();

	        ApiException apiException = ApiException.notFound(reason, message);

	        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createConflictException_ShouldSetFieldsCorrectly() {
	        String reason = "Conflict Reason";
	        String message = "Conflict Message";
	        Integer statusCode = HttpStatus.CONFLICT.value();

	        ApiException apiException = ApiException.conflict(reason, message);

	        assertEquals(HttpStatus.CONFLICT.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createPreconditionFailedException_ShouldSetFieldsCorrectly() {
	        String reason = "Precondition Failed Reason";
	        String message = "Precondition Failed Message";
	        Integer statusCode = HttpStatus.PRECONDITION_FAILED.value();

	        ApiException apiException = ApiException.preconditionFailed(reason, message);

	        assertEquals(HttpStatus.PRECONDITION_FAILED.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createInternalErrorException_ShouldSetFieldsCorrectly() {
	        String reason = "Internal Error Reason";
	        String message = "Internal Error Message";
	        Integer statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

	        ApiException apiException = ApiException.internalError(reason, message);

	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createNotImplementedException_ShouldSetFieldsCorrectly() {
	        String reason = "Not Implemented Reason";
	        String message = "Not Implemented Message";
	        Integer statusCode = HttpStatus.NOT_IMPLEMENTED.value();

	        ApiException apiException = ApiException.notImplemented(reason, message);

	        assertEquals(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createServiceUnavailableException_ShouldSetFieldsCorrectly() {
	        String reason = "Service Unavailable Reason";
	        String message = "Service Unavailable Message";
	        Integer statusCode = HttpStatus.SERVICE_UNAVAILABLE.value();

	        ApiException apiException = ApiException.serviceUnavailable(reason, message);

	        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }

	    @Test
	    void createAcceptedException_ShouldSetFieldsCorrectly() {
	        String reason = "Accepted Reason";
	        String message = "Accepted Message";
	        Integer statusCode = HttpStatus.ACCEPTED.value();

	        ApiException apiException = ApiException.accepted(reason, message);

	        assertEquals(HttpStatus.ACCEPTED.getReasonPhrase(), apiException.getCode());
	        assertEquals(reason, apiException.getReason());
	        assertEquals(message, apiException.getMessage());
	        assertEquals(statusCode, apiException.getStatusCode());
	    }
    
}
