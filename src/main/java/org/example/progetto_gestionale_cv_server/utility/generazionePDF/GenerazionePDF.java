package org.example.progetto_gestionale_cv_server.utility.generazionePDF;

import jakarta.persistence.criteria.Root;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class GenerazionePDF {

    // stabilisci il percorso nel quale salvare il pdf creato
    private String getPath(Users utente, CVs cv) {

        String RootPath = Paths.get("").toAbsolutePath().toString();
        return RootPath + "/src/main/resources/static/" + utente.getNome() + "_" + utente.getCognome() + "_" + UUID.randomUUID() + ".pdf";
    }

    public void CreazionePDFFileSystem(Users utente, CVs cv) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                StringBuilder formattedText = new StringBuilder();
                formattedText.append("Formatted Content: ciao ciao");

                contentStream.beginText();
                contentStream.setFont(PDType1Font.COURIER, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(formattedText.toString());
                contentStream.endText();
            }

            document.save(this.getPath(utente, cv));
            

        } catch (IOException ex) {
            throw new IOException("errore durante la creazione del PDF:" + ex.getMessage());
        }
    }

//    private byte[] generatePdfData(String content) {
//        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            try(PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, 12);
//                contentStream.newLineAtOffset(50, 700);
//                contentStream.showText(content);
//                contentStream.endText();
//            }
//
//            document.save(outputStream);
//            return outputStream.toByteArray();
//        }catch (IOException e) {
//            throw new RuntimeException("Errore durante la generazione del PDF", e);
//        }
//    }
}
