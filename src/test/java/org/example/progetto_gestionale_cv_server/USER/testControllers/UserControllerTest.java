package org.example.progetto_gestionale_cv_server.USER.testControllers;

import org.example.progetto_gestionale_cv_server.USER.DTOs.req.Edit_utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.TokenResponse;
import org.example.progetto_gestionale_cv_server.USER.controllers.UserController;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.utility.StringResponse.StringResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void RegistrazioneAdminAndataABuonaSegnoRispostaSpecifica() {

        RegistrazioneUtenteDTO dataMockRegistrazione = new RegistrazioneUtenteDTO();

        when(this.userService.registrazioneUtente(dataMockRegistrazione)).thenReturn(true);

        ResponseEntity<StringResponse> response = this.userController.registrazione(dataMockRegistrazione);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("utente_admin_creato_con_successo.", response.getBody().getMessage());
    }

    @Test
    void RegistrazioneUserStandardRispostaSpecificaNelBody() {
        RegistrazioneUtenteDTO dataMockRegistrazione = new RegistrazioneUtenteDTO();

        when(this.userService.registrazioneUtente(dataMockRegistrazione)).thenReturn(false);

        ResponseEntity<StringResponse> response = this.userController.registrazione(dataMockRegistrazione);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("utente_creato_con_successo.", response.getBody().getMessage());
    }

    @Test
    void lanciaErroreProvenienteDalService() {
        RegistrazioneUtenteDTO dataMockRegistrazione = new RegistrazioneUtenteDTO();

        when(this.userService.registrazioneUtente(dataMockRegistrazione))
                .thenThrow(new RuntimeException("errore"));

        ResponseEntity<StringResponse> response = this.userController.registrazione(dataMockRegistrazione);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    void LoginEffettuatoConSuccessoRitornaOK() {

        LoginDTO mockLogin = new LoginDTO();
        mockLogin.setEmail("prova@gmail.it");
        mockLogin.setPassword("password");
        TokenResponse tokenRespMock = new TokenResponse("defaultToken", "token generato con successo.");

        when(this.userService.doLogin(mockLogin)).thenReturn("defaultToken");
        ResponseEntity<TokenResponse> response = this.userController.login(mockLogin);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getToken(), tokenRespMock.getToken());
    }

    @Test
    void LoginErratoRitornaStatus_internal_server_error() {
        LoginDTO mockLogin = new LoginDTO();
        mockLogin.setEmail("prova@gmail.it");
        mockLogin.setPassword("password");

        TokenResponse mockResp = new TokenResponse(null, "errore in fase di login:");

        when(this.userService.doLogin(mockLogin)).thenThrow(new RuntimeException("errore mockato"));


        ResponseEntity<TokenResponse> response = this.userController.login(mockLogin);

        assertEquals(Objects.requireNonNull(response.getBody()).getToken(), mockResp.getToken());
        assertNull(response.getBody().getToken());

    }

    @Test
    void UtenteModificaIpropriDatiConSuccesso() {
        Edit_utente_DTO editDati = new Edit_utente_DTO();
        Long id_utente = 1L;
        when(this.userService.editUtente(editDati, id_utente)).thenReturn(true);

        ResponseEntity<StringResponse> response = this.userController.modificaUtente(editDati, id_utente);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Utente modificato con successo.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void ritornaMessaggioUtenteAttivato() {

        Long id_utente = 1L;

        when(this.userService.handleStatus(id_utente)).thenReturn(true);

        ResponseEntity<StringResponse> response = this.userController.ChangeActiveStatusUtente(id_utente);
        assertEquals("Utente attivato.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void ritornaMessaggioUtenteDisattivato() {
        Long id_utente = 1L;

        when(this.userService.handleStatus(id_utente)).thenReturn(false);

        ResponseEntity<StringResponse> response = this.userController.ChangeActiveStatusUtente(id_utente);
        assertEquals("Utente disattivato.", Objects.requireNonNull(response.getBody()).getMessage());
    }
}
