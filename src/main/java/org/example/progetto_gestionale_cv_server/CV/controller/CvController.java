package org.example.progetto_gestionale_cv_server.CV.controller;

import jakarta.validation.Valid;
import org.example.progetto_gestionale_cv_server.CV.DTOs.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.service.CvService;
import org.example.progetto_gestionale_cv_server.CV.service.ICvService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/cv")
public class CvController {

    private final ICvService cvService;

    //costrutt
    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    // creazione record cv e relativo file .pdf su file system
    @PostMapping("/create")
    public ResponseEntity<String> GeneraPdF(@Valid @RequestBody DatiCreazionePDF_DTO datiCreazionePDFDto) {
        try {
            this.cvService.creaPDF_Record_CV(datiCreazionePDFDto);
            return new ResponseEntity<>("Curriculum creato con successo. ", HttpStatus.OK);

        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>("Errore durante la creazione del curriculum: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // modifica record cv e relativo file .pdf su file system
    // (è una modifica finta, viene cancellato e ricreato sulla base dei nuovi dati forniti dall utente)
    @PostMapping("edit")
    public ResponseEntity<String> modificaPDF(@Valid @RequestBody DatiCreazionePDF_DTO datiModificaPDFDto) {
        try {
            this.cvService.modificaPDF_Record_CV(datiModificaPDFDto);
            return new ResponseEntity<>("curriculum modificato con successo.", HttpStatus.OK);
        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>("Errore durante la modifica del curriculum: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
