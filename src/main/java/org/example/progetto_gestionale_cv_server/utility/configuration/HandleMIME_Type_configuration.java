package org.example.progetto_gestionale_cv_server.utility.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.IOException;

// questa classe dovrebbe in teoria, ogni volta che un client richiede un immagine statica,
// salvata in questo caso nel percorso static/images
// questo metodo prende la richiesta in entrata dal client
// e imposta nell header il mime type come di tipo file immagine e non text/plain
public class HandleMIME_Type_configuration extends ResourceHttpRequestHandler {
    // @Override
    protected void writeContent(HttpServletRequest request, HttpServletResponse response, Resource resource) throws IOException {
        String mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
//        super.writeContent(request, response, resource);
    }
}
