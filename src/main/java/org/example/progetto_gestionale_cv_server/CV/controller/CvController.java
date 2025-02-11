package org.example.progetto_gestionale_cv_server.CV.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.example.progetto_gestionale_cv_server.CV.DTOs.*;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Get_All_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.service.CvService;
import org.example.progetto_gestionale_cv_server.CV.service.ICvService;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Cv_Msg_response;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @DeleteMapping("/delete")
    public ResponseEntity<String> cancellaCV(@Valid @RequestBody ID_UTENTE_CV_DTO ids_utente_cv) {
        try {
            this.cvService.CancellaCV(ids_utente_cv);
            return new ResponseEntity<>("cv cancellato con successo.", HttpStatus.OK);
        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>("errore durante la cancellazione del curriculum: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/get")
    public ResponseEntity<Cv_Msg_response> getCV(@Valid @RequestBody ID_UTENTE_CV_DTO ids_utente_cv) {
        try {

            BaseDTO cvDTO = this.cvService.getCv(ids_utente_cv);
            return new ResponseEntity<>(new Cv_Msg_response(cvDTO, null), HttpStatus.OK);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(new Cv_Msg_response(null, "errore durante il reperimento del cv: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // specifico che il parametro in entrata sia l'id dell utente , tipo numerico e non null
    @GetMapping("/get-all/{id}")
    public ResponseEntity<Get_All_cv_DTO> getAllCV(@PathVariable("id") @NotNull @NumberFormat Long id_utente) {
        try {
            List<BaseDTO> listaCV = this.cvService.getAll_CV(id_utente);
            return new ResponseEntity<>(new Get_All_cv_DTO(listaCV, null), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new Get_All_cv_DTO(null, "Errore durante il reperimento di tutti i curriculum relativi all'utente."), HttpStatus.OK);
        }
    }
}
