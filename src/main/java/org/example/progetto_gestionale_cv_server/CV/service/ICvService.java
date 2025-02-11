package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;

import java.io.IOException;
import java.util.List;

public interface ICvService {
    void creaPDF_Record_CV(DatiCreazionePDF_DTO datiCreazionePDFDto) throws IOException;

    void modificaPDF_Record_CV(DatiCreazionePDF_DTO datiModificaPDF) throws IOException;

    void CancellaCV(ID_UTENTE_CV_DTO datiCancellaCV) throws IOException;

    BaseDTO getCv(ID_UTENTE_CV_DTO dati_id);

    List<BaseDTO> getAll_CV(Long id_utente);
}
