package org.example.progetto_gestionale_cv_server.USER.testServices;

import org.apache.catalina.User;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private MapperUser mapperUser; // Add this line

    @InjectMocks
    private UserService userService;

//    private RegistrazioneUtenteDTO mockRegistrazione;
//
//    @BeforeEach
//    void simulaDTORegistrazione() {
//        RegistrazioneUtenteDTO datiMock_registrazione = new RegistrazioneUtenteDTO();
//        datiMock_registrazione.setEmail("esempio@aruba.it");
//        datiMock_registrazione.setCognome("marcellino");
//        datiMock_registrazione.setNome("marco");
//        datiMock_registrazione.setPassword("lamiapassword");
//        datiMock_registrazione.setTelefono("+39 333 3454456");
//        datiMock_registrazione.setConsensoTrattamentoDati(true);
//        this.mockRegistrazione = datiMock_registrazione;
//    }

    @Test
    void seRegistroUtenteStandardRitornaFalse() {

        RegistrazioneUtenteDTO datiMock_registrazione = new RegistrazioneUtenteDTO();
        datiMock_registrazione.setEmail("esempio@aruba.it");
        datiMock_registrazione.setCognome("marcellino");
        datiMock_registrazione.setNome("marco");
        datiMock_registrazione.setPassword("lamiapassword");
        datiMock_registrazione.setTelefono("+39 333 3454456");
        datiMock_registrazione.setConsensoTrattamentoDati(true);

        boolean result = this.userService.registrazioneUtente(datiMock_registrazione);
        assertFalse(result);
    }

    @Test
    void seRegistroUtenteAdminRitornaTrue() {

        RegistrazioneUtenteDTO datiMock_regis_admin = new RegistrazioneUtenteDTO();
        datiMock_regis_admin.setEmail("admin@gmail.com");
        datiMock_regis_admin.setCognome("performanceAdmin");
        datiMock_regis_admin.setNome("performanceAdmin");
        datiMock_regis_admin.setPassword("Ar11091995!.!");
        datiMock_regis_admin.setTelefono("+39 111 1111111");
        datiMock_regis_admin.setConsensoTrattamentoDati(true);
        
        boolean result = this.userService.registrazioneUtente(datiMock_regis_admin);
        assertTrue(result);
    }
}
