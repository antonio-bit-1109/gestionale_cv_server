package org.example.progetto_gestionale_cv_server.IMAGES.controller;

import jakarta.validation.constraints.NotNull;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_Utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Resp_get_utente_DTO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class imageController {

    private final String imageDirectory = "src/main/resources/static/images/"; // Percorso delle immagini
    // Mappa per associare le estensioni MIME corrette
    private static final Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("png", "image/png");
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("jpeg", "image/jpeg");
        mimeTypes.put("gif", "image/gif");
        mimeTypes.put("webp", "image/webp");
    }


    // endpoint per fare la get delle immagini, incaricando il server di specificare, nella response,
    // che il tipo di file che verrà servito al browser sarà un formato immagine, e non un generico plain/text
    @GetMapping("/get/{image_name}")
    public ResponseEntity<Resource> getImage(@PathVariable("image_name") String name_img) {
        try {
            // Costruisce il percorso dell'immagine
            Path imagePath = Paths.get(imageDirectory + name_img).toAbsolutePath().normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            // Controlla se il file esiste
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Ottiene l'estensione del file per determinare il Content-Type
            String fileExtension = getFileExtension(name_img);
            String contentType = mimeTypes.getOrDefault(fileExtension, "application/octet-stream");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);

        } catch (RuntimeException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileExtension(String fileName) {
        int lastpoint = fileName.lastIndexOf(".");
        return fileName.substring(lastpoint);
    }
}
