package org.example.progetto_gestionale_cv_server.USER.testControllers;

import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.controllers.UserController;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTestControllers {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void RegistrazioneAdminAndataABuonaSegnoRispostaSpecifica() {

        RegistrazioneUtenteDTO dataMockRegistrazione = new RegistrazioneUtenteDTO();

        when(this.userService.registrazioneUtente(dataMockRegistrazione)).thenReturn(true);

        ResponseEntity<String> response = this.userController.registrazione(dataMockRegistrazione);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("utente admin creato con successo.", response.getBody());
    }

    @Test
    void RegistrazioneUserStandardRispostaSpecificaNelBody() {
        RegistrazioneUtenteDTO dataMockRegistrazione = new RegistrazioneUtenteDTO();

        when(this.userService.registrazioneUtente(dataMockRegistrazione)).thenReturn(false);

        ResponseEntity<String> response = this.userController.registrazione(dataMockRegistrazione);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("utente creato con successo.", response.getBody());
    }

    @Test
    void lanciaErroreProvenienteDalService() {
        RegistrazioneUtenteDTO dataMockRegistrazione = new RegistrazioneUtenteDTO();

        when(this.userService.registrazioneUtente(dataMockRegistrazione))
                .thenThrow(new RuntimeException("errore"));

        ResponseEntity<String> response = this.userController.registrazione(dataMockRegistrazione);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
