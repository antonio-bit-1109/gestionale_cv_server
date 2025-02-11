package org.example.progetto_gestionale_cv_server.ADMIN.service;

import org.apache.catalina.mapper.Mapper;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperCv;
import org.example.progetto_gestionale_cv_server.utility.UTILITYPDF.PDFExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class AdminService implements IAdminService {

    private MapperCv mapperCv;

    public AdminService(MapperCv mapperCv) {
        this.mapperCv = mapperCv;
    }

    // metodo per salvare un .pdf su file system non collegato a uno user
    @Override
    public void savePDForfano(MultipartFile file) throws IOException {

        String RootPath = Paths.get("").toAbsolutePath().toString()
                + "/src/main/resources/static/notLinkedCv";
        Path uploadPath = Paths.get(RootPath);

        // crea la directory se non esiste
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Ottieni il filepath
        Path filePath = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));

        // se il file path inizia come mi aspetto ovvero "cv vitae di ...nome cognome"
        // allora carico il file, altrimenti lancio errore
        Files.write(filePath, file.getBytes());

        String extractedText = PDFExtractor.extractTextFromPDF(filePath.toString());
        this.checkDocumentTitle(extractedText);


        // SALVA NUOVO CV SENZA RIFERIMENTO ALL UTENTE
        this.ExtractDataFromPDF(extractedText);
    }

    private void checkDocumentTitle(String extractedText) {

        String controlString = "CurriculumVitaedi";

        if (!(extractedText.toLowerCase().trim().contains(controlString.toLowerCase()))) {
            throw new RuntimeException("il documento caricato deve avere un titolo tipo: Curriculum Vitae di 'nome' 'cognome'");
        }
    }

    private void ExtractDataFromPDF(String extractedText) {
        System.out.println(extractedText);
    }
}
