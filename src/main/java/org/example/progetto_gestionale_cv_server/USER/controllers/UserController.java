package org.example.progetto_gestionale_cv_server.USER.controllers;

import jakarta.validation.Valid;
import org.example.progetto_gestionale_cv_server.USER.DTOs.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.utility.Responses.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userservice) {
        this.userService = userservice;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registrazione(@Valid @RequestBody RegistrazioneUtenteDTO datiRegistrazione) {
        try {
            this.userService.registrazioneUtente(datiRegistrazione);
            return new ResponseEntity<>("utente creato con successo.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Errore in fase di registrazione: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = this.userService.doLogin(loginDTO);
            return new ResponseEntity<>(new TokenResponse(token, "token generato con successo."), HttpStatus.OK);
        } catch (RuntimeException ex) {

            return new ResponseEntity<>(new TokenResponse(null, "errore in fase di login:" + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
