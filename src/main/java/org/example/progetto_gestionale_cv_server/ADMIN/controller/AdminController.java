package org.example.progetto_gestionale_cv_server.ADMIN.controller;

import jakarta.persistence.criteria.Root;
import org.example.progetto_gestionale_cv_server.ADMIN.service.AdminService;
import org.example.progetto_gestionale_cv_server.ADMIN.service.IAdminService;
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

    @PostMapping("/assignment-cv")
    public ResponseEntity<String> caricaCvPdf(
            @RequestParam("file") MultipartFile file,
            @RequestPart("id_utente") String id_utente,
            @RequestPart("titolo") String titolo,
            @RequestPart("competenze") String competenze,
            @RequestPart("descrizione_generale") String descrizione_generale,
            @RequestPart("esperienze_precedenti") String esperienze_precedenti,
            @RequestPart("istruzione") String istruzione,
            @RequestPart("lingue_conosciute") String lingue_conosciute
    ) {

        if (file.isEmpty()) {
            return new ResponseEntity<>("nessun file inviato", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("file pdf salvato con successo", HttpStatus.OK);

        } catch (RuntimeException | IOException e) {
            return new ResponseEntity<>("errore durante il salvataggio del file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
}
