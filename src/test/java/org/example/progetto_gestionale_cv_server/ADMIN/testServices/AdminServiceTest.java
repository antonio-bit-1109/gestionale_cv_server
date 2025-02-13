package org.example.progetto_gestionale_cv_server.ADMIN.testServices;

import org.example.progetto_gestionale_cv_server.ADMIN.service.AdminService;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperCv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    MapperCv mapperCv;

    @Mock
    CvRepository cvRepository;

    @InjectMocks
    AdminService adminService;

//    @BeforeAll
//    static void deleteFilesInDirectory() throws IOException {
//        String PercorsoSuServer = Paths.get("").toAbsolutePath().toString()
//                + "/src/main/resources/static/notLinkedCv";
//        Path uploadPath = Paths.get(PercorsoSuServer);
//
//        // crea la directory se non esiste
//        if (Files.exists(uploadPath)) {
//            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(uploadPath)) {
//                for (Path path : directoryStream) {
//                    Files.delete(path);
//                }
//            }
//        }
//    }

//    @Test
//    void quandoSalvoUnRecordCvOrfanoRitornaTrue() throws IOException {
//        CVs cv = new CVs();
//        HashMap<String, String> mappa = new HashMap<>();
//        MultipartFile file = new MockMultipartFile(
//                "file", // nome del parametro
//                "test.pdf", // nome del file
//                "application/pdf", // tipo di contenuto
//                "Questo è il contenuto del file".getBytes() // contenuto del file
//        );
//
//        doNothing().when(this.mapperCv).createCv(mappa, file, "percorso su server Mock");
//        when(this.cvRepository.save(cv)).thenReturn(cv);
//
//        boolean result = this.adminService.savePDForfano(file, mappa);
//        assertTrue(result);
//    }
}
