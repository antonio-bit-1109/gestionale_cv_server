package org.example.progetto_gestionale_cv_server.utility.generazionePDF;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.persistence.criteria.Root;
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
        String nameFilePDF = utente.getNome() + "_" + utente.getCognome() + "_" + UUID.randomUUID() + ".pdf";
        return RootPath + "/src/main/resources/static/" + nameFilePDF;
    }

    public void CancellaPDF_file_System(String path_location_pdf) throws IOException {
        Path path = Paths.get(path_location_pdf);
        Files.delete(path);
    }

    public void CreazionePDFFileSystem(Users utente, CVs cv, boolean alreadyCvPresent) throws IOException {

        if (alreadyCvPresent) {
            Path path = Paths.get(cv.getNome_file_pdf());
            Files.delete(path);
        }

        String dest = this.getPath(utente, cv);

        Path path = Paths.get(dest);
        // setto l'Sindirizzo di dove verrà salvato il file direttamente nel metodo;
        cv.setNome_file_pdf(dest);


        try (PdfWriter writer = new PdfWriter(dest);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

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
                cv.setProfileImage(imagePath);
            } catch (IOException e) {
                throw new IOException("Errore durante il caricamento dell'immagine: " + e.getMessage(), e);
            }


            // Left-aligned CV data
            document.add(new Paragraph(new Text("Nome: ").setBold()).add(utente.getNome()));
            document.add(new Paragraph("Cognome: " + utente.getCognome()));
            document.add(new Paragraph("Titolo: " + cv.getTitolo()));
            document.add(new Paragraph("Esperienze Precedenti: " + cv.getEsperienze_Precedenti()));
            document.add(new Paragraph("Competenze: " + cv.getCompetenze()));
            document.add(new Paragraph("Istruzione: " + cv.getIstruzione()));
            document.add(new Paragraph("Descrizione generale: " + cv.getDescrizioneGenerale()));
            document.add(new Paragraph("Lingue conosciute: " + cv.getLingueConosciute()));

            this.cvRepository.save(cv);
            this.userRepository.save(utente);
        } catch (IOException e) {
            throw new IOException("Errore durante la creazione del PDF: " + e.getMessage(), e);
        }
    }

}
