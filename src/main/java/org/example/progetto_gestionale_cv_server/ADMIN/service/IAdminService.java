package org.example.progetto_gestionale_cv_server.ADMIN.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IAdminService {
    // metodo per salvare un .pdf su file system non collegato a uno user
    boolean savePDFeAssegna(MultipartFile file, HashMap<String, String> mappaParti) throws IOException;

}
