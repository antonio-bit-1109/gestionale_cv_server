package org.example.progetto_gestionale_cv_server.ADMIN.testControllers;

import org.example.progetto_gestionale_cv_server.ADMIN.controller.AdminController;
import org.example.progetto_gestionale_cv_server.ADMIN.service.IAdminService;
import org.example.progetto_gestionale_cv_server.utility.StringResponse.StringResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Mock
    private IAdminService adminService;

    @InjectMocks
    AdminController adminController;

    private MultipartFile mockFile;
    private HashMap<String, String> mapMock;

    @BeforeEach
    void setUpFile() {
        this.mockFile = new MockMultipartFile("file", "test.pdf",
                "application/pdf", "test content".getBytes());
    }

    @BeforeEach
    void setUpMap() {
        this.mapMock = new HashMap<>();
        mapMock.put("id_utente", "1");
        mapMock.put("titolo", "titolo");
        mapMock.put("competenze", "competenze");
        mapMock.put("descrizione_generale", "descrizione_generale");
        mapMock.put("esperienze_precedenti", "esperienze_precedenti");
        mapMock.put("istruzione", "istruzione");
        mapMock.put("lingue_conosciute", "lingue_conosciute");
    }

    @Test
    void ritornaUnaBadRequestAlClient() {

        MultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);
        ResponseEntity<StringResponse> response = adminController.caricaCvPdf(
                emptyFile, "1", "titolo", "competenze", "descrizione_generale", "esperienze_precedenti", "istruzione", "lingue_conosciute"
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("nessun file inviato", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void ritornaOkSeCvCaricatoSunSuccesso() throws IOException {

        when(this.adminService.savePDFeAssegna(this.mockFile, this.mapMock)).thenReturn(true);

        ResponseEntity<StringResponse> response = this.adminController.caricaCvPdf(
                this.mockFile, "1", "titolo", "competenze", "descrizione_generale", "esperienze_precedenti", "istruzione", "lingue_conosciute"
        );
        assertEquals("file pdf salvato con successo", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

