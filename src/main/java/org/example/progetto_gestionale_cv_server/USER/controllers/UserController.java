package org.example.progetto_gestionale_cv_server.USER.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.Response;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.CambioImgProfilo_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_Utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Resp_get_utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.service.IUserService;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    public UserController(UserService userservice) {
        this.userService = userservice;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registrazione(@Valid @RequestBody RegistrazioneUtenteDTO datiRegistrazione) {
        try {
            boolean adminCreated = this.userService.registrazioneUtente(datiRegistrazione);

            if (adminCreated) {
                return new ResponseEntity<>("utente admin creato con successo.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("utente creato con successo.", HttpStatus.OK);
            }

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

    @PostMapping("/upload/photo")
    public ResponseEntity<String> caricamentoNuovaImgProfilo(
            @NotNull @RequestParam("file") MultipartFile file,
            @NotNull @RequestPart("id_utente") String id_utente) {
        try {

            Long idUser = Long.parseLong(id_utente);

            this.userService.cambioImgProfilo(file, idUser);
            return new ResponseEntity<>("immagine del profilo caricata con successo.", HttpStatus.OK);

        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>("Errore durante il caricamento dell'immagine:" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    // endpoint per reperire dati utente
    @GetMapping("/get/{id_utente}")
    public ResponseEntity<Resp_get_utente_DTO> getUtente(@NotNull @PathVariable Long id_utente) {
        try {

            Get_Utente_DTO utenteDTO = this.userService.GetUtenteSingolo(id_utente);
            return new ResponseEntity<>(new Resp_get_utente_DTO(null, utenteDTO), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(new Resp_get_utente_DTO("Errore durante il reperimento dell'utente selezionato: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // endpoint che fare get di tutti utenti
//    @GetMapping("/get/all")
//    public ResponseEntity<> getAllUtenti(){
//        try {
//
//
//        } catch (RuntimeException ex) {
//
//        }
//    }
//
//
////    Endpoint per la modifica di un particolare utente
//    @PostMapping("/edit")
//    public ResponseEntity<> modificaUtente(){
//        try {
//
//
//        } catch (RuntimeException ex) {
//
//        }
//    }
//
//
//    // soft delete utente, campo isActive false;
//    @PostMapping("/edit")
//    public ResponseEntity<> SoftDeleteUtente(){
//        try {
//
//
//        } catch (RuntimeException ex) {
//
//        }
//    }
//
}
