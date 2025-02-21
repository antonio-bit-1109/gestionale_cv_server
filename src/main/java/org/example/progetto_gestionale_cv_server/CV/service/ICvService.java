package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiModifica_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.util.List;

public interface ICvService {

    boolean creaPDF_Record_CV(DatiCreazionePDF_DTO datiCreazionePDFDto) throws IOException;

    boolean modificaPDF_Record_CV(DatiModifica_cv_DTO datiModificaPDF) throws IOException;

    boolean CancellaCV(ID_UTENTE_CV_DTO ids_utente_cv) throws IOException;

    BaseDTO getCv(ID_UTENTE_CV_DTO dati_id);

    List<BaseDTO> getAll_CV(Long id_utente);

    CVs returnCvIfExist(Long id_cv);

    List<BaseDTO> findByCompetenza(String competenza);

    List<BaseDTO> trovaCvDalNomeUtente(String nome);

    List<BaseDTO> trovaCvDalleEsperienze(String esperienze);

    Resource downloadCurriculum(String id_cv);

    String getFileName();
}
