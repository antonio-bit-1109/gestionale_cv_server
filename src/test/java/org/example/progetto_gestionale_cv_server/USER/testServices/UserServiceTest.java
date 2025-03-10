package org.example.progetto_gestionale_cv_server.USER.testServices;

import org.apache.catalina.User;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.repository.CredenzialiRepository;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.Edit_utente_DTO;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void QuandoUtenteCaricaNuovaImgProfiloTornaTrue() throws IOException {

        MultipartFile mockFile = new MockMultipartFile(
                "file", // name of the file
                "test.jpg", // original filename
                "image/jpeg", // content type
                "test image content".getBytes() // file content
        );

        Users utente = new Users();

        when(this.userRepository.findById(1L)).thenReturn(Optional.of(utente));
        when(this.userRepository.save(utente)).thenReturn(utente);

        boolean result = this.userService.cambioImgProfilo(mockFile, 1L);
        assertTrue(result);
    }


    @Test
    void QuandoMOdificoDatiUtenteRitornaTrue() {

        Edit_utente_DTO editDati = new Edit_utente_DTO();
        Long id_utente = 1L;
        Users utente = new Users();
        Credenziali credenziali = new Credenziali();
        utente.setNome("prova");
        utente.setCognome("prova");
        utente.setTelefono("+39 333 5476685");
        utente.setProfileImage("default");
        utente.setConsensoTrattamentoDati(true);
        credenziali.setEmail("prova@gmail.com");
        credenziali.setRole("USER");
        credenziali.setPassword("password");
        utente.setCredenziali(credenziali);
        credenziali.setUser(utente);


        when(this.userRepository.findById(id_utente)).thenReturn(Optional.of(utente));
        when(this.credenzialiRepository.findByEmail("prova@gmail.com")).thenReturn(Optional.of(credenziali));
        doNothing().when(this.mapperUser).editingUserData(editDati, utente, credenziali);


        boolean result = this.userService.editUtente(editDati, id_utente);
        assertTrue(result);
    }
}
