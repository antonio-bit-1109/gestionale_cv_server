package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.DatiCreazionePDF_DTO;

import java.io.IOException;

public interface ICvService {
    void creaPDF(DatiCreazionePDF_DTO datiCreazionePDFDto) throws IOException;
}
