package org.example.progetto_gestionale_cv_server.utility.UTILITYPDF;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class GenerazionePDF {

    private final UserRepository userRepository;
    private final CvRepository cvRepository;

    public GenerazionePDF(UserRepository userRepo, CvRepository cvRepo) {
        this.userRepository = userRepo;
        this.cvRepository = cvRepo;
    }

    // stabilisci il percorso nel quale salvare il pdf creato
    public String getPath(Users utente, CVs cv) {

        String RootPath = Paths.get("").toAbsolutePath().toString();
        String nameFilePDF = this.generateNamePDF(utente);
        return RootPath + "/src/main/resources/static/" + nameFilePDF;
    }

    private String generateNamePDF(Users utente) {
        return utente.getNome() + "_" + utente.getCognome() + "_" + UUID.randomUUID() + ".pdf";
    }


    public void CancellaPDF_file_System(CVs cv) throws IOException {
        String RootPath = Paths.get("").toAbsolutePath().toString();
        // Path path = Paths.get(cv.getNome_file_pdf());
//        Files.delete(Path.of(RootPath + "/src/main/resources/static/" + cv.getNome_file_pdf()));
        try {
            this.tentaCancellazione(RootPath, cv, "/src/main/resources/static/");

        } catch (IOException e) {
            try {
                this.tentaCancellazione(RootPath, cv, "/src/main/resources/static/LoadedFromAdmin/");
            } catch (IOException ex) {
                throw new RuntimeException("impossibile trovare il percorso per cancellare il file: " + ex.getMessage());
            }
        }
    }

    private void tentaCancellazione(String percorsoFile, CVs cv, String percorsoCartelle) throws IOException {
        Files.delete(Path.of(percorsoFile + percorsoCartelle + cv.getNome_file_pdf()));
    }

    public void CreazionePDFFileSystem(Users utente, CVs cv, boolean alreadyCvPresent) throws IOException {

        if (alreadyCvPresent) {
            this.CancellaPDF_file_System(cv);
        }

        String dest = this.getPath(utente, cv);

        // Path path = Paths.get(dest);
        // setto l'Sindirizzo di dove verrà salvato il file direttamente nel metodo;

        int index = dest.indexOf("static") + 7;
        String nomeFilePdf = dest.substring(index);
        cv.setNome_file_pdf(nomeFilePdf);


        try (PdfWriter writer = new PdfWriter(dest);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            //FORMATTAZIONE DEL PDF

            // Centered title
            Paragraph title = new Paragraph("Curriculum Vitae di " + utente.getNome() + " " + utente.getCognome())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(18);
            document.add(title);

            // Add the image
            String imagePath = Paths.get("").toAbsolutePath() + "/src/main/resources/static/images/default.jpg";
            try {
                ImageData imageData = ImageDataFactory.create(imagePath);
                Image image = new Image(imageData).setWidth(200).setHeight(200);
                document.add(image);

            } catch (IOException e) {
                throw new IOException("Errore durante il caricamento dell'immagine: " + e.getMessage(), e);
            }


            // Left-aligned CV data
            document.add(new Paragraph(new Text("Nome: ").setBold()).add(utente.getNome() + ";"));
            document.add(new Paragraph(new Text("Cognome: ").setBold()).add(utente.getCognome() + ";"));
            document.add(new Paragraph(new Text("Titolo: ").setBold()).add(cv.getTitolo() + ";"));
            document.add(new Paragraph(new Text("Esperienze Precedenti: ").setBold()).add(cv.getEsperienze_Precedenti() + ";"));
            document.add(new Paragraph(new Text("Competenze: ").setBold()).add(cv.getCompetenze() + ";"));
            document.add(new Paragraph(new Text("Istruzione: ").setBold()).add(cv.getIstruzione() + ";"));
            document.add(new Paragraph(new Text("Descrizione generale: ").setBold()).add(cv.getDescrizioneGenerale() + ";"));
            document.add(new Paragraph(new Text("Lingue conosciute: ").setBold()).add(cv.getLingueConosciute() + ";"));

            this.cvRepository.save(cv);
            this.userRepository.save(utente);
        } catch (IOException e) {
            throw new IOException("Errore durante la creazione del PDF: " + e.getMessage(), e);
        }
    }

}
