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

    @Test
    void ritornaTrueSeCaricamentoPdfAbuonFine() throws IOException {

        HashMap<String, String> mapMock = new HashMap<>();

        MultipartFile mockFile = new MockMultipartFile(
                "file", // name of the file
                "test.jpg", // original filename
                "image/jpeg", // content type
                "test image content".getBytes() // file content
        );

        String PercorsoSuServer = Paths.get("").toAbsolutePath().toString()
                + "/src/main/resources/static/LoadedFromAdmin";

        doNothing().when(this.mapperCv).createCv(mapMock, mockFile, PercorsoSuServer);

        boolean result = this.adminService.savePDFeAssegna(mockFile, mapMock);
        assertTrue(result);
    }
}
