package org.example.progetto_gestionale_cv_server.ADMIN.service;

import jakarta.persistence.criteria.Root;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperCv;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class AdminService implements IAdminService {

    private final MapperCv mapperCv;


    public AdminService(MapperCv mapperCv) {
        this.mapperCv = mapperCv;

    }


    // metodo per salvare un .pdf su file system non collegato a uno user
    @Override
    public void savePDForfano(MultipartFile file, HashMap<String, String> mappaParti) throws IOException {

        String PercorsoSuServer = Paths.get("").toAbsolutePath().toString()
                + "/src/main/resources/static/notLinkedCv";
        Path uploadPath = Paths.get(PercorsoSuServer);

        // crea la directory se non esiste
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Ottieni il filepath
        Path filePath = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));

        // carico il file su server
        Files.write(filePath, file.getBytes());

        // creo un record cv "orfano" sul db
        // per far questo richiamo nel metodo il file per prendere il nome con cui questo è stato caricato
        // e il rootPath per creare il percorso completo del file da salvare nel record su db
        this.mapperCv.createCv(mappaParti, file, PercorsoSuServer);
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
