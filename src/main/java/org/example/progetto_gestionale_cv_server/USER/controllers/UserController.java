package org.example.progetto_gestionale_cv_server.USER.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.Response;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.CambioImgProfilo_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.Edit_utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_List_utenti_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_Utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Resp_get_utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.service.IUserService;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.TokenResponse;
import org.example.progetto_gestionale_cv_server.utility.StringResponse.StringResponse;
import org.example.progetto_gestionale_cv_server.utility.customExceptions.EmailAlreadyUsed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    public UserController(UserService userservice) {
        this.userService = userservice;
    }


    // registrazione utente, in fase di registrazione criptazione password utente -- Bcrypt
    @PostMapping("/registration")
    public ResponseEntity<StringResponse> registrazione(@Valid @RequestBody RegistrazioneUtenteDTO datiRegistrazione) {
        try {
            boolean adminCreated = this.userService.registrazioneUtente(datiRegistrazione);

            if (adminCreated) {
                return new ResponseEntity<>(new StringResponse("utente_admin_creato_con_successo."), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new StringResponse("utente_creato_con_successo."), HttpStatus.OK);
            }

        } catch (RuntimeException e) {
            return new ResponseEntity<>(new StringResponse("Errore in fase di registrazione: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // login utente per ritorno al client del token generato.
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            String token = this.userService.doLogin(loginDTO);
            return new ResponseEntity<>(new TokenResponse(token, "token generato con successo."), HttpStatus.OK);
        } catch (RuntimeException ex) {

            return new ResponseEntity<>(new TokenResponse(null, "errore in fase di login:" + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // endpoint per caricamento foto utente su file system
    @PostMapping("/upload/photo")
    public ResponseEntity<StringResponse> caricamentoNuovaImgProfilo(
            @NotNull @RequestParam("file") MultipartFile file,
            @NotNull @RequestPart("id_utente") String id_utente) {
        try {

            Long idUser = Long.parseLong(id_utente);

            this.userService.cambioImgProfilo(file, idUser);
            return new ResponseEntity<>(new StringResponse("immagine del profilo caricata con successo."), HttpStatus.OK);

        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>(new StringResponse("Errore durante il caricamento dell'immagine:" + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

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
    @GetMapping("/get/all")
    public ResponseEntity<Get_List_utenti_DTO> getAllUtenti() {
        try {

            List<Get_Utente_DTO> listaUtenti = this.userService.getListaUtenti();
            return new ResponseEntity<>(new Get_List_utenti_DTO(null, listaUtenti), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(new Get_List_utenti_DTO("Errore durante il reperimento degli utenti registrati in piattaforma: " + ex.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /// /    Endpoint per la modifica di un particolare utente
    @PostMapping("/edit/{id_utente}")
    public ResponseEntity<StringResponse> modificaUtente(@Valid @RequestBody Edit_utente_DTO datiEdit, @PathVariable Long id_utente) {
        try {
            this.userService.editUtente(datiEdit, id_utente);
            return new ResponseEntity<>(new StringResponse("Utente modificato con successo."), HttpStatus.OK);
        } catch (RuntimeException ex) {

            return new ResponseEntity<>(new StringResponse("errore durante la modifica dell'utente: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //    // soft delete utente, campo isActive false;
    @GetMapping("/handleStatus/{id_utente}")
    public ResponseEntity<StringResponse> ChangeActiveStatusUtente(@NotNull @PathVariable Long id_utente) {
        try {

            boolean changeStatus = this.userService.handleStatus(id_utente);

            return changeStatus ?
                    new ResponseEntity<>(new StringResponse("Utente attivato."), HttpStatus.OK) :
                    new ResponseEntity<>(new StringResponse("Utente disattivato."), HttpStatus.OK);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(new StringResponse("Errore durante la cancellazione dell'utente: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get/profile/image/{id}")
    public ResponseEntity<StringResponse> getURLProfileImage(@NotNull @PathVariable("id") Long id_utente) {
        try {
            String imageURL = this.userService.getProfileImage(id_utente);
            return new ResponseEntity<>(new StringResponse(imageURL), HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(new StringResponse("Errore durante la get dell'immagine profilo dell'utente: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
//
}
