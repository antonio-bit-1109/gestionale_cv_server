package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.DatiCreazionePDF_DTO;

import java.io.IOException;

public interface ICvService {
    void creaPDF_Record_CV(DatiCreazionePDF_DTO datiCreazionePDFDto) throws IOException;

    void modificaPDF_Record_CV(DatiCreazionePDF_DTO datiModificaPDF) throws IOException;

    void CancellaCV(ID_UTENTE_CV_DTO datiCancellaCV) throws IOException;

    BaseDTO getCv(ID_UTENTE_CV_DTO dati_id);
}
