package org.example.progetto_gestionale_cv_server.ADMIN.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IAdminService {
    void savePDForfano(MultipartFile file) throws IOException;
}
