package org.example.progetto_gestionale_cv_server.utility.UTILITYPDF;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;

public class PDFExtractor {

    // metodo della libreria pdfbox per estrarre il contenuto di un file pdf in formato stringa
    // ATTUALMENTE NON UTILIZZATO
    @Bean
    public static String extractTextFromPDF(String filePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}
