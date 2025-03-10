package org.example.progetto_gestionale_cv_server.ADMIN.controller;

import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import org.example.progetto_gestionale_cv_server.ADMIN.service.AdminService;
import org.example.progetto_gestionale_cv_server.ADMIN.service.IAdminService;
import org.example.progetto_gestionale_cv_server.utility.StringResponse.StringResponse;
import org.example.progetto_gestionale_cv_server.utility.UTILITYPDF.PDFExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    // endpoint per il caricamento del file pdf
    // riceve un form data con appesi dati interni al cv
    @PostMapping("/assignment")
    public ResponseEntity<StringResponse> caricaCvPdf(
            @NotNull @RequestParam("file") MultipartFile file,
            @NotNull @RequestPart("id_utente") String id_utente,
            @NotNull @RequestPart("titolo") String titolo,
            @NotNull @RequestPart("competenze") String competenze,
            @NotNull @RequestPart("descrizione_generale") String descrizione_generale,
            @NotNull @RequestPart("esperienze_precedenti") String esperienze_precedenti,
            @NotNull @RequestPart("istruzione") String istruzione,
            @NotNull @RequestPart("lingue_conosciute") String lingue_conosciute
    ) {

        if (file.isEmpty()) {
            return new ResponseEntity<>(new StringResponse("nessun file inviato"), HttpStatus.BAD_REQUEST);
        }

        try {

            HashMap<String, String> mappaParti = new HashMap<>();
            mappaParti.put("id_utente", id_utente);
            mappaParti.put("titolo", titolo);
            mappaParti.put("competenze", competenze);
            mappaParti.put("descrizione_generale", descrizione_generale);
            mappaParti.put("esperienze_precedenti", esperienze_precedenti);
            mappaParti.put("istruzione", istruzione);
            mappaParti.put("lingue_conosciute", lingue_conosciute);

            this.adminService.savePDFeAssegna(file, mappaParti);
            return new ResponseEntity<>(new StringResponse("file pdf salvato con successo"), HttpStatus.OK);

        } catch (RuntimeException | IOException e) {
            return new ResponseEntity<>(new StringResponse("errore durante il salvataggio del file: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
}
