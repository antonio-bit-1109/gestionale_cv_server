package org.example.progetto_gestionale_cv_server.CV.testController;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiModifica_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Cv_Msg_response;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Get_All_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.controller.CvController;
import org.example.progetto_gestionale_cv_server.CV.service.CvService;
import org.example.progetto_gestionale_cv_server.utility.StringResponse.StringResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CvControllerTest {

    @Mock
    private CvService cvService;

    @InjectMocks
    private CvController cvController;


    @Test
    void AllaCreazioneDiUnCvConSuccessoRitornaResp() throws IOException {

        DatiCreazionePDF_DTO daticreazMock = new DatiCreazionePDF_DTO();
        when(this.cvService.creaPDF_Record_CV(daticreazMock)).thenReturn(true);

        ResponseEntity<StringResponse> response = this.cvController.GeneraPdF(daticreazMock);
        assertEquals("Curriculum creato con successo. ", response.getBody().getMessage());
    }

    @Test
    void TornaErroreNellaResponse() throws IOException {

        DatiCreazionePDF_DTO daticreazMock = new DatiCreazionePDF_DTO();
        when(this.cvService.creaPDF_Record_CV(daticreazMock))
                .thenThrow(new RuntimeException("errore creazione cv"));

        ResponseEntity<StringResponse> response = this.cvController.GeneraPdF(daticreazMock);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void ritornaREspSuccessoAModificaCV() throws IOException {
        DatiModifica_cv_DTO modificaMock = new DatiModifica_cv_DTO();

        when(this.cvService.modificaPDF_Record_CV(modificaMock))
                .thenReturn(true);

        ResponseEntity<StringResponse> response = this.cvController.modificaPDF(modificaMock);
        assertEquals("curriculum modificato con successo.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void ritornaRespConMessaggioDiErrore() throws IOException {
        DatiModifica_cv_DTO modificaMock = new DatiModifica_cv_DTO();

        when(this.cvService.modificaPDF_Record_CV(modificaMock))
                .thenThrow(new RuntimeException("errore modifica cv"));

        ResponseEntity<StringResponse> response = this.cvController.modificaPDF(modificaMock);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void cancellaCVConSuccesso() throws IOException {
        ID_UTENTE_CV_DTO idsUtenteCvMock = new ID_UTENTE_CV_DTO();
        when(this.cvService.CancellaCV(idsUtenteCvMock)).thenReturn(true);

        ResponseEntity<StringResponse> response = this.cvController.cancellaCV(idsUtenteCvMock);
        assertEquals("cv cancellato con successo.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void cancellaCVConErrore() throws IOException {
        ID_UTENTE_CV_DTO idsUtenteCvMock = new ID_UTENTE_CV_DTO();
        doThrow(new RuntimeException("errore cancellazione cv")).when(this.cvService).CancellaCV(idsUtenteCvMock);

        ResponseEntity<StringResponse> response = this.cvController.cancellaCV(idsUtenteCvMock);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getCVConSuccesso() {
//        ID_UTENTE_CV_DTO idsUtenteCvMock = new ID_UTENTE_CV_DTO();
        Long id_cv = 1L;
        BaseDTO cvDTOMock = new BaseDTO();

        when(this.cvService.getCv(id_cv)).thenReturn(cvDTOMock);

        ResponseEntity<Cv_Msg_response> response = this.cvController.getCV(id_cv);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void getCVConErrore() {
        Long id_cv = 1L;
        when(this.cvService.getCv(id_cv)).thenThrow(new RuntimeException("errore reperimento cv"));

        ResponseEntity<Cv_Msg_response> response = this.cvController.getCV(id_cv);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    void getAllCVConSuccesso() {
        Long idUtente = 1L;
        List<BaseDTO> listaCVMock = List.of(new BaseDTO());
        when(this.cvService.getAll_CV(idUtente)).thenReturn(listaCVMock);

        ResponseEntity<Get_All_cv_DTO> response = this.cvController.getAllCV(idUtente);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getListaCV());
    }

    @Test
    void getAllCVConErrore() {
        Long idUtente = 1L;
        when(this.cvService.getAll_CV(idUtente)).thenThrow(new RuntimeException("errore reperimento cv"));

        ResponseEntity<Get_All_cv_DTO> response = this.cvController.getAllCV(idUtente);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getMessage());
        assertNull(response.getBody().getListaCV());
    }
}
