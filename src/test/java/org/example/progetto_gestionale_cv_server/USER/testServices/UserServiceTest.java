package org.example.progetto_gestionale_cv_server.USER.testServices;

import org.apache.catalina.User;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.repository.CredenzialiRepository;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperUser;
import org.example.progetto_gestionale_cv_server.utility.generateToken.GenerateToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CredenzialiRepository credenzialiRepository;

    @Mock
    private MapperUser mapperUser;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GenerateToken generateToken;

    @InjectMocks
    private UserService userService;


    @Test
    void SeFaccioLoginEUtenteNonEsisteRitornaErroreRuntime() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("email@example.it");
        loginDTO.setPassword("mypassword");

        when(this.credenzialiRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.doLogin(loginDTO);
        });

        assertEquals("l'utente con le credenziali selezionate non esiste.", ex.getMessage());
        verify(credenzialiRepository).findByEmail(loginDTO.getEmail());
    }

    @Test
    void ChiamatoIlLoginRitorniUnaSringaConToken() {

        LoginDTO loginMock = new LoginDTO();
        loginMock.setPassword("passwordProva");
        loginMock.setEmail("prova@gmail.com");


        Users utenteMock = new Users();
        utenteMock.setId(1L);
        utenteMock.setNome("alvaro");
        utenteMock.setCognome("vitali");
        utenteMock.setTelefono("+39 333 5467762");
        utenteMock.setConsensoTrattamentoDati(true);
        utenteMock.setProfileImage("default");


        Credenziali credenzialiTest = new Credenziali();
        credenzialiTest.setRole("ADMIN");
        credenzialiTest.setEmail("prova@gmail.com");
        credenzialiTest.setPassword("$2a$10$D9Q9Q9Q9Q9Q9Q9Q9Q9Q9QO");

        utenteMock.setCredenziali(credenzialiTest);
        credenzialiTest.setUser(utenteMock);

        when(this.credenzialiRepository.findByEmail(credenzialiTest.getEmail())).thenReturn(Optional.of(credenzialiTest));
        doNothing().when(mapperUser).confirmPassword(credenzialiTest, loginMock.getPassword());

        when(this.generateToken.tokenGeneration(utenteMock)).thenReturn("tokenDefault");

        String result = this.userService.doLogin(loginMock);
        assertEquals("tokenDefault", result);

    }

    @Test
    void seRegistroUtenteRitornaTrue() {
        RegistrazioneUtenteDTO registrazioneMock = new RegistrazioneUtenteDTO();
        registrazioneMock.setNome("prova");
        registrazioneMock.setCognome("prova");
        registrazioneMock.setEmail("admin@gmail.com");
        registrazioneMock.setTelefono("+39 111 1111111");
        registrazioneMock.setConsensoTrattamentoDati(true);
        registrazioneMock.setPassword("Ar11091995!.!");

        when(this.mapperUser.isCreatingAnAdmin(registrazioneMock)).thenReturn(true);
        when(this.userService.MapUtenteToEntity(registrazioneMock, true)).thenReturn(true);

        boolean result = this.userService.registrazioneUtente(registrazioneMock);
        assertTrue(result);
    }

    @Test
    void seCreaUnAdminTornaTrue() {

        RegistrazioneUtenteDTO registrazioneMock = new RegistrazioneUtenteDTO();
        registrazioneMock.setNome("performanceAdmin");
        registrazioneMock.setCognome("performanceAdmin");
        registrazioneMock.setEmail("admin@gmail.com");
        registrazioneMock.setTelefono("+39 111 1111111");
        registrazioneMock.setConsensoTrattamentoDati(true);
        registrazioneMock.setPassword("Ar11091995!.!");

        when(this.mapperUser.isCreatingAnAdmin(registrazioneMock)).thenReturn(true);
        when(this.mapperUser.FromDTOToEntity(registrazioneMock, true)).thenReturn(true);

        boolean result = this.userService.registrazioneUtente(registrazioneMock);
        assertTrue(result);
    }

    @Test
    void seCreaUnoUserStandardTornaFalse() {

        RegistrazioneUtenteDTO registrazioneMock = new RegistrazioneUtenteDTO();
        registrazioneMock.setNome("ciao");
        registrazioneMock.setCognome("ciao");
        registrazioneMock.setEmail("admin@gmail.com");
        registrazioneMock.setTelefono("+39 111 1111111");
        registrazioneMock.setConsensoTrattamentoDati(true);
        registrazioneMock.setPassword("Ar11091995!.!");

        when(this.mapperUser.isCreatingAnAdmin(registrazioneMock)).thenReturn(false);
        when(this.mapperUser.FromDTOToEntity(registrazioneMock, false)).thenReturn(false);

        boolean result = this.userService.registrazioneUtente(registrazioneMock);
        assertFalse(result);
    }

    @Test
    void QuandoUtenteCaricaNuovaImgProfiloTornaTrue() {

    }
}
