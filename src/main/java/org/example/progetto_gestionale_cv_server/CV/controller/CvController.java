package org.example.progetto_gestionale_cv_server.CV.controller;

import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.example.progetto_gestionale_cv_server.CV.DTOs.*;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiModifica_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Get_All_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.service.CvService;
import org.example.progetto_gestionale_cv_server.CV.service.ICvService;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Cv_Msg_response;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_List_utenti_DTO;
import org.example.progetto_gestionale_cv_server.utility.StringResponse.StringResponse;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<StringResponse> GeneraPdF(@Valid @RequestBody DatiCreazionePDF_DTO datiCreazionePDFDto) {
        try {
            this.cvService.creaPDF_Record_CV(datiCreazionePDFDto);
            return new ResponseEntity<>(new StringResponse("Curriculum creato con successo. "), HttpStatus.OK);

        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>(new StringResponse("Errore durante la creazione del curriculum: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // modifica record cv e relativo file .pdf su file system
    // (è una modifica finta, viene cancellato e ricreato sulla base dei nuovi dati forniti dall utente)
    @PostMapping("edit")
    public ResponseEntity<StringResponse> modificaPDF(@Valid @RequestBody DatiModifica_cv_DTO datiModificaPDFDto) {
        try {
            this.cvService.modificaPDF_Record_CV(datiModificaPDFDto);
            return new ResponseEntity<>(new StringResponse("curriculum modificato con successo."), HttpStatus.OK);
        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>(new StringResponse("Errore durante la modifica del curriculum: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/delete")
    public ResponseEntity<StringResponse> cancellaCV(
            @Valid @RequestBody ID_UTENTE_CV_DTO ids_utente_cv) {
        try {
            this.cvService.CancellaCV(ids_utente_cv);
            return new ResponseEntity<>(new StringResponse("cv cancellato con successo."), HttpStatus.OK);
        } catch (RuntimeException | IOException ex) {
            return new ResponseEntity<>(new StringResponse("errore durante la cancellazione del curriculum: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id_cv}")
    public ResponseEntity<Cv_Msg_response> getCV(@NotNull @PathVariable Long id_cv) {
        try {

            BaseDTO cvDTO = this.cvService.getCv(id_cv);
            return new ResponseEntity<>(new Cv_Msg_response(cvDTO, null), HttpStatus.OK);

        } catch (RuntimeException ex) {
            return new ResponseEntity<>(new Cv_Msg_response(null, "errore durante il reperimento del cv: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // specifico che il parametro in entrata sia l'id dell utente , tipo numerico e non null
    // prendo tutti i cv di uno specifico utente
    @GetMapping("/get-all/{id}")
    public ResponseEntity<Get_All_cv_DTO> getAllCV(@PathVariable("id") @NotNull @NumberFormat Long id_utente) {
        try {
            List<BaseDTO> listaCV = this.cvService.getAll_CV(id_utente);
            return new ResponseEntity<>(new Get_All_cv_DTO(listaCV, null), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new Get_All_cv_DTO(null, "Errore durante il reperimento di tutti i curriculum relativi all'utente."), HttpStatus.OK);
        }
    }

    @GetMapping("/findByCompetenza")
    public ResponseEntity<Get_All_cv_DTO> FindUsersByCompetenza(
            @RequestParam("competenza") String competenza
    ) {

        try {
            List<BaseDTO> ListaCvCompetenze = this.cvService.findByCompetenza(competenza);
            return new ResponseEntity<>(new Get_All_cv_DTO(ListaCvCompetenze, null), HttpStatus.OK);

        } catch (RuntimeException e) {

            return new ResponseEntity<>(new Get_All_cv_DTO
                    (null, "Errore durante il reperimento di tutti i curriculum relativi alla competenza " + competenza + " : " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/findByNome")
    public ResponseEntity<Get_All_cv_DTO> trovaCvTramiteNome(
            @RequestParam("nome") String nome
    ) {
        try {
            List<BaseDTO> listaCvPerNome = this.cvService.trovaCvDalNomeUtente(nome);
            return new ResponseEntity<>(new Get_All_cv_DTO(listaCvPerNome, null), HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(new Get_All_cv_DTO
                    (null, "Errore durante il reperimento di tutti i curriculum filtrati per nome " + nome + " : " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/findByEsperienze")
    public ResponseEntity<Get_All_cv_DTO> trovaCvTramiteEsperienze(
            @RequestParam("esperienze") String esperienze
    ) {
        try {
            List<BaseDTO> listaCvPerEsperienze = this.cvService.trovaCvDalleEsperienze(esperienze);
            return new ResponseEntity<>(new Get_All_cv_DTO(listaCvPerEsperienze, null), HttpStatus.OK);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(new Get_All_cv_DTO
                    (null, "Errore durante il reperimento di tutti i curriculum filtrati per nome " + esperienze + " : " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @GetMapping("/downloadPDF")
    public ResponseEntity<Resource> downloadPdf(
            @RequestParam("id_cv") String id_cv) {

        try {
            Resource resource = cvService.downloadCurriculum(id_cv);

            if (resource == null) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + this.cvService.getFileName());
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}

