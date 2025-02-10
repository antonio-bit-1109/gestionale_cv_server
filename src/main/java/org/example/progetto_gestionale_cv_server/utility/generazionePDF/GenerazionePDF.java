package org.example.progetto_gestionale_cv_server.utility.generazionePDF;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.persistence.criteria.Root;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class GenerazionePDF {

    // stabilisci il percorso nel quale salvare il pdf creato
    public String getPath(Users utente, CVs cv) {

        String RootPath = Paths.get("").toAbsolutePath().toString();
//        return RootPath + "/src/main/resources/static/" ;
        String nameFilePDF = utente.getNome() + "_" + utente.getCognome() + "_" + UUID.randomUUID() + ".pdf";
        return RootPath + "/src/main/resources/static/" + nameFilePDF;
    }

    public void CreazionePDFFileSystem(Users utente, CVs cv) throws IOException {
        String dest = this.getPath(utente, cv);
        try (PdfWriter writer = new PdfWriter(dest);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Centered title
            Paragraph title = new Paragraph("Curriculum Vitae di " + utente.getNome() + " " + utente.getCognome())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(18);
            document.add(title);

//            document.add(new Paragraph("Nome: ").setBold()).add(new Paragraph(new Text(utente.getNome())));
            // Left-aligned CV data
            document.add(new Paragraph("Nome: " + utente.getNome()));
            document.add(new Paragraph("Cognome: " + utente.getCognome()));
            document.add(new Paragraph("Titolo: " + cv.getTitolo()));
            document.add(new Paragraph("Esperienze Precedenti: " + cv.getEsperienze_Precedenti()));
            document.add(new Paragraph("Competenze: " + cv.getCompetenze()));
            document.add(new Paragraph("Istruzione: " + cv.getIstruzione()));
            document.add(new Paragraph("Descrizione generale: " + cv.getDescrizioneGenerale()));
            document.add(new Paragraph("Lingue conosciute: " + cv.getLingueConosciute()));
        } catch (IOException e) {
            throw new IOException("Errore durante la creazione del PDF: " + e.getMessage(), e);
        }
    }
//    public void CreazionePDFFileSystem(Users utente, CVs cv) throws IOException {
//        try (PDDocument document = new PDDocument(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
//
//            PDPage page = new PDPage();
//            document.addPage(page);
//            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
//
//                contentStream.beginText();
//
//                // Centered title
//                PDRectangle pageSize = page.getMediaBox();
//                float pageWidth = pageSize.getWidth();
//                String title = "Curriculum Vitae di" + utente.getNome() + " " + utente.getCognome();
//                PDFont titleFont = PDType1Font.HELVETICA_BOLD;
//                float titleFontSize = 18;
//                float titleWidth = titleFont.getStringWidth(title) / 1000 * titleFontSize;
//                float titleXOffset = (pageWidth - titleWidth) / 2;
//                contentStream.setFont(titleFont, titleFontSize);
//                contentStream.newLineAtOffset(titleXOffset, 750);
//                contentStream.showText(title);
//                contentStream.endText();
//
//                // Left-aligned CV data
//                contentStream.beginText();
//                contentStream.setFont(PDType1Font.HELVETICA, 12);
//                contentStream.newLineAtOffset(50, 700);
//                contentStream.showText("Nome: " + utente.getNome());
//                contentStream.newLineAtOffset(0, -15);
//                contentStream.showText("Cognome: " + utente.getCognome());
//                contentStream.newLineAtOffset(0, -15);
//                contentStream.showText("Titolo: " + cv.getTitolo());
//                contentStream.newLineAtOffset(0, -15);
//                contentStream.showText("Esperienze Precedenti: " + cv.getEsperienze_Precedenti());
//                contentStream.newLineAtOffset(0, -15);
//                contentStream.showText("Competenze: " + cv.getCompetenze());
//                contentStream.newLineAtOffset(0, -15);
//                contentStream.showText("Istruzione: " + cv.getIstruzione());
//                contentStream.endText();
//            }
//
//            document.save(this.getPath(utente, cv));
//
//
//        } catch (IOException ex) {
//            throw new IOException("errore durante la creazione del PDF:" + ex.getMessage());
//        }
//    }

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
