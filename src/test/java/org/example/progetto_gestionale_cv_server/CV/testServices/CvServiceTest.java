package org.example.progetto_gestionale_cv_server.CV.testServices;

import jakarta.validation.constraints.NotNull;
import org.apache.catalina.User;
import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiModifica_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Cv_get_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.CV.service.CvService;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperCv;
import org.example.progetto_gestionale_cv_server.utility.UTILITYPDF.GenerazionePDF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CvServiceTest {


    @Mock
    private MapperCv mapperCv;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CvRepository cvRepository;

    @Mock
    private GenerazionePDF generazionePDF;

    @Mock
    private UserService userService;

    @InjectMocks
    private CvService cvService;


    @Test
    void RitornaListaDiTuttiCvAppartenentiUtente() {

        Instant currentTime = Instant.now();
        Long userId = 1L;
        Users userMock = new Users();

        BaseDTO mockDto = new BaseDTO();

        List<CVs> listaCv = new ArrayList<>();
        CVs cv = new CVs();
        listaCv.add(cv);
        listaCv.add(cv);

        userMock.setListaCv(listaCv);


        when(this.userService.returnUserIfExist(userId)).thenReturn((userMock));
        when(this.mapperCv.fromEntityToDTO(cv)).thenReturn(mockDto);

        List<BaseDTO> result = this.cvService.getAll_CV(userId);
        assertInstanceOf(List.class, result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    void ritornaILCvSelezionatoDallUtente() {

//        ID_UTENTE_CV_DTO mock_id_user_cv = new ID_UTENTE_CV_DTO();
//        mock_id_user_cv.setId_utente(1L);
//        mock_id_user_cv.setId_cv(1L);
        Long id_cv = 1L;

        CVs cv = new CVs();
        BaseDTO mockDto = new BaseDTO();

        when(this.cvRepository.findById(id_cv)).thenReturn(Optional.of(cv));
        when(this.mapperCv.fromEntityToDTO(cv)).thenReturn(mockDto);
        BaseDTO result = this.cvService.getCv(id_cv);
        assertInstanceOf(BaseDTO.class, result);
    }

    @Test
    void quandoCancellaCvRitornaTrue() throws IOException {

        ID_UTENTE_CV_DTO mock_id_user_cv = new ID_UTENTE_CV_DTO();
        mock_id_user_cv.setId_utente(1L);
        mock_id_user_cv.setId_cv(1L);

        Users userAssociato = new Users();
        userAssociato.setId(1L);

        CVs cvMock = new CVs();
        cvMock.setUser(userAssociato);

        when(this.cvRepository.findById(mock_id_user_cv.getId_cv())).thenReturn(Optional.of(cvMock));
        doNothing().when(this.cvRepository).delete(cvMock);

        boolean result = this.cvService.CancellaCV(mock_id_user_cv);
        assertTrue(result);
    }

    @Test
    void quandoCreaPdfRitornaTrue() throws IOException {


        DatiCreazionePDF_DTO mockReq = new DatiCreazionePDF_DTO();
        mockReq.setIdUtente(1L);
        Users userMock = new Users();
        userMock.setId(1L);
        userMock.setListaCv(new ArrayList<>());
        CVs cv_mock = new CVs();

        when(this.mapperCv.FromDTOToEntity(mockReq)).thenReturn(cv_mock);
        when(this.userService.returnUserIfExist(userMock.getId())).thenReturn((userMock));
        when(this.cvRepository.save(cv_mock)).thenReturn(cv_mock);
        when(this.userRepository.save(userMock)).thenReturn(new Users()); // Corrected this line
        doNothing().when(this.generazionePDF).CreazionePDFFileSystem(userMock, cv_mock, false);

        boolean result = this.cvService.creaPDF_Record_CV(mockReq);
        assertTrue(result);
    }

    @Test
    void quandoModificoUnPdfRitornaTrue() throws IOException {

        DatiModifica_cv_DTO mockDatiModifica = new DatiModifica_cv_DTO();
        mockDatiModifica.setIdUtente(1L);
        mockDatiModifica.setIdCv(1L);

        Users utenteMock = new Users();

        when(this.userService.returnUserIfExist(mockDatiModifica.getIdUtente())).thenReturn((utenteMock));
        doNothing().when(this.mapperCv).ModificaCv(mockDatiModifica, utenteMock);

        boolean result = this.cvService.modificaPDF_Record_CV(mockDatiModifica);
        assertTrue(result);
    }
}
