package wcc.assignment.arthur_leong;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("type", "GET Postcode");
        errorDetails.put("title", "Invalid Postcode");
        errorDetails.put("status", ex.getStatusCode().value());
        errorDetails.put("error", ex.getReason());
        errorDetails.put("instance", "/postCode");

        return new ResponseEntity<>(errorDetails, ex.getStatusCode());
    }
}
