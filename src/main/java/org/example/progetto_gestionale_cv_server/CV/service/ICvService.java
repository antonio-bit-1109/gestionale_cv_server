package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.ModificaDatiPDF_DTO;

import java.io.IOException;

public interface ICvService {
    void creaPDF_Record_CV(DatiCreazionePDF_DTO datiCreazionePDFDto) throws IOException;

    void modificaPDF_Record_CV(ModificaDatiPDF_DTO datiModificaPDF) throws IOException;
}
