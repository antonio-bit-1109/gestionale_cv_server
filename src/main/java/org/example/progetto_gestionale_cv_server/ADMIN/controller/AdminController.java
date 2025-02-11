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
import java.util.Objects;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IAdminService adminService;

    public AdminController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> caricaCvPdf(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return new ResponseEntity<>("nessun file inviato", HttpStatus.BAD_REQUEST);
        }

        try {

            this.adminService.savePDForfano(file);
            return new ResponseEntity<>("file salvato con successo", HttpStatus.OK);

        } catch (RuntimeException | IOException e) {
            return new ResponseEntity<>("errore durante il salvataggio del file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
}
