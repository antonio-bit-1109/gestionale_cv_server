package org.example.progetto_gestionale_cv_server.utility.validationDTO;

import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.utility.customExceptions.CustomPatternException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// questa classe è un vero e proprio controller che scatta quando viene rilevato un errore
@RestControllerAdvice
public class ValidationDTO {

    //questa classe intercetta ogni DTO che arriva su un controller e, se la relativa DTO presenta delle annotation
// che non vengono rispettate, genera una stringa di errore che viene re-inviata al client.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // questi metodi possono essere programmati per lanciare un errore custom
    // e inviare nella response una mappa key value per mostrare un errore specifico
    @ExceptionHandler(CustomPatternException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentExceptions(CustomPatternException ex) {
        Map<String, String> errMap = new HashMap<>();
        errMap.put(ex.getKeyField(), ex.getMessage());

        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }
}


