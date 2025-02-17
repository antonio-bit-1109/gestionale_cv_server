package org.example.progetto_gestionale_cv_server.USER.testControllers;

import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // IMPORT CORRETTO
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; // IMPORT CORRETTO
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class userControllerTestSpringBootMockChiamata {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext WebapplicationContext;

    @BeforeEach
    void setUpMVC() {
        mockMvc = MockMvcBuilders.webAppContextSetup(WebapplicationContext).build();
    }


    @Test
    void RegistrazioneUtenteStandardAndataABuonaSegnoRispostaSpecifica() throws Exception {

        String registrazioneJson = "{\n" +
                "    \"nome\": \"lorenza\",\n" +
                "   \"cognome\": \"presti\" ,\n" +
                "    \"email\": \"hhh@hotmail.it\" , \n" +
                "    \"telefono\": \"+39 333 5371163\",\n" +
                "    \"password\": \"LOREnzina95!\" , \n" +
                "    \"consensoTrattamentoDati\" : true \n" +
                "}";

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrazioneJson))
                .andExpect(status().isOk())
                .andExpect(content().string("utente creato con successo."));
    }


    @Test
    void RegistrazioneUtenteAdminAndataABuonSegno() throws Exception {
        String registrazioneJson = "{\n" +
                "    \"nome\": \"performanceAdmin\",\n" +
                "   \"cognome\": \"performanceAdmin\" ,\n" +
                "    \"email\": \"admin@gmail.com\" , \n" +
                "    \"telefono\": \"+39 111 1111111\",\n" +
                "    \"password\": \"Ar11091995!.!\" ,\n" +
                "    \"consensoTrattamentoDati\" : true\n" +
                "}";

        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrazioneJson))
                .andExpect(status().isOk())
                .andExpect(content().string("utente admin creato con successo."));
    }


    @Test
    void RegistrazioneSimulandoFormatoTelefonoNonCorretto() throws Exception {

        String BodyRequest = "{\n" +
                "    \"nome\": \"lorenza\",\n" +
                "   \"cognome\": \"presti\" ,\n" +
                "    \"email\": \"bbb@hotmail.it\" , \n" +
                "    \"telefono\": \"+393335371163\",\n" +
                "    \"password\": \"LOREnzina95!\" , \n" +
                "    \"consensoTrattamentoDati\" : true \n" +
                "}";


        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BodyRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"telefono\":\" il telefono deve essere nel formato" +
                        " +39 XXX XXXXXXX\"}"));


    }


    @Test
    void LaLoginRitornaErroreseMancaEmail() throws Exception {

        String bodyReq = "{\n" +
                " \"email\": \"antonio@gmail.it\" ,   \n" +
                "\"password\": \"Ar11091995!.!\"\n" +
                "}";

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyReq))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"token\":null,\"msg\":\"errore in fase di login:" +
                        "l'utente con le credenziali selezionate non esiste.\"}"));


    }


    @Test
    void laPasswordDeveEssereNelFormatoCorrettoAltrimentiErroreValidazione() throws Exception {
        String reqBody = "{\n" +
                " \"email\": \"antonio@gmail.it\" ,   \n" +
                "\"password\": \"ciaociao\"\n" +
                "}";

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqBody))
                .andExpect(status().isBadRequest());

    }

}
