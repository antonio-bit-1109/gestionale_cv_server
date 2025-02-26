package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiModifica_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperCv;
import org.example.progetto_gestionale_cv_server.utility.UTILITYPDF.GenerazionePDF;
import org.example.progetto_gestionale_cv_server.utility.customExceptions.FileDoesntExist;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CvService implements ICvService {

    private final CvRepository cvRepository;
    private final UserRepository userRepository;
    private final MapperCv mapperCv;
    private final GenerazionePDF generazionePDF;
    private final UserService userService;
    private String fileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public CvService(CvRepository cvRepo, UserRepository userRepository, MapperCv mapperCv, GenerazionePDF generazionePDF, UserService userService) {
        this.cvRepository = cvRepo;
        this.userRepository = userRepository;
        this.mapperCv = mapperCv;
        this.generazionePDF = generazionePDF;
        this.userService = userService;

    }

    //metodo di creazione del pdf e popolamento tabella cv
    @Override
    public boolean creaPDF_Record_CV(DatiCreazionePDF_DTO datiCreazionePDFDto) throws RuntimeException, IOException {

        CVs cv = this.mapperCv.FromDTOToEntity(datiCreazionePDFDto);
        Users utente = this.userService.returnUserIfExist(datiCreazionePDFDto.getIdUtente());


        cv.setUser(utente); // Set the user before saving the CV
        CVs cvSaved = this.cvRepository.save(cv);
        utente.getListaCv().add(cvSaved); // Add the CV to the user's list of CVs
        this.userRepository.save(utente);

        this.generazionePDF.CreazionePDFFileSystem(utente, cvSaved, false);
        return true;
    }


    // metodo che dovrà modificare i campi cv della tabella e sostituire il pdf con i dati aggiornati.
    @Override
    public boolean modificaPDF_Record_CV(DatiModifica_cv_DTO datiModificaPDF) throws IOException {
        Users user = this.userService.returnUserIfExist(datiModificaPDF.getIdUtente());
        this.mapperCv.ModificaCv(datiModificaPDF, user);
        return true;
    }

    @Transactional
    @Override
    public boolean CancellaCV(ID_UTENTE_CV_DTO ids_utente_cv) throws IOException {

        CVs cv = this.returnCvIfExist(ids_utente_cv.getId_cv());

        if (cv.getUser().getId().equals(ids_utente_cv.getId_utente())) {
            this.cvRepository.delete(cv);
            this.generazionePDF.CancellaPDF_file_System(cv);
            return true;
        } else {
            throw new RuntimeException("l'utente che sta cercando di eliminare questo cv non è il proprietario del cv.");
        }

    }

    @Override
    public BaseDTO getCv(Long id_cv) {
//        Users utente = this.userService.returnUserIfExist(dati_id.getId_utente());
//
//        for (CVs cV : utente.getListaCv()) {
//            if (cV.getId().equals(dati_id.getId_cv())) {
//                return this.mapperCv.fromEntityToDTO(cV);
//            }
//        }

        Optional<CVs> cvOpt = this.cvRepository.findById(id_cv);

        if (cvOpt.isEmpty()) {
            throw new RuntimeException("impossibile trovare il cv specificato");

        }

        CVs cv = cvOpt.get();
        return this.mapperCv.fromEntityToDTO(cv);
    }

    @Override
    public List<BaseDTO> getAll_CV(Long id_utente) {
        Users user = this.userService.returnUserIfExist(id_utente);

        List<BaseDTO> listaCV = new ArrayList<>();

        for (CVs cv : user.getListaCv()) {
            BaseDTO cvDTO = this.mapperCv.fromEntityToDTO(cv);
            listaCV.add(cvDTO);
        }

        return listaCV;
    }


    @Override
    // metodo che ritorna un cv se esiste altrimenti lancia errore
    public CVs returnCvIfExist(Long id_cv) {
        Optional<CVs> cvOpt = this.cvRepository.findById(id_cv);

        if (cvOpt.isEmpty()) {
            throw new RuntimeException("cv non trovato.");
        }

        return cvOpt.get();
    }

    @Override
    public List<BaseDTO> findByCompetenza(String competenza) {

        List<BaseDTO> listaCvDto = new ArrayList<>();

        List<CVs> listaCv = this.cvRepository.findByCompetenzeContainingIgnoreCase(competenza);

        for (CVs cv : listaCv) {
            listaCvDto.add(this.mapperCv.fromEntityToDTO(cv));
        }
        return listaCvDto;
    }


    @Override
    public List<BaseDTO> trovaCvDalNomeUtente(String nome) {

        List<Users> listaUtenti = this.userService.returnAllUsers();
        List<BaseDTO> listaCv = new ArrayList<>();

        listaUtenti.stream()
                .filter(user -> user.getNome().toLowerCase().contains(nome.toLowerCase()))
                .forEach(users -> {
                    users.getListaCv().forEach(cVEntity -> {
                        listaCv.add(this.mapperCv.fromEntityToDTO(cVEntity));
                    });
                });

        return listaCv;

    }

    @Override
    public List<BaseDTO> trovaCvDalleEsperienze(String esperienze) {

        List<CVs> listaCv = this.cvRepository.findAll();
        List<BaseDTO> listaCvDTO = new ArrayList<>();

        listaCv.forEach(cv -> {
            if (cv.getEsperienze_Precedenti().toLowerCase().contains(esperienze.replace("_", " "))) {
                listaCvDTO.add(this.mapperCv.fromEntityToDTO(cv));
            }
        });

        return listaCvDTO;

    }

    @Override
    public Resource downloadCurriculum(String id_cv) {
        try {

            CVs cv = returnCvIfExist(Long.parseLong(id_cv));

            String filePath = Paths.get("").toAbsolutePath().toString();
            Path path = Paths.get(filePath + "/src/main/resources/static/" + cv.getNome_file_pdf());
            File file = path.toFile();

            if (!file.exists()) {

                try {
                    Path path2 = Paths.get(filePath + "/src/main/resources/static/LoadedFromAdmin/" + cv.getNome_file_pdf());
                    File file2 = path2.toFile();
                    setFileName(cv.getNome_file_pdf());
                    return new FileSystemResource(file2);
                } catch (RuntimeException e) {
                    throw new FileDoesntExist("il file non esiste neanche nel secondo path.", "download file");

                }
                // throw new FileDoesntExist("il file non esiste", "download file");
            }

            setFileName(cv.getNome_file_pdf());
            return new FileSystemResource(file);
        } catch (Exception e) {

            throw new RuntimeException("errore durante il download del file:" + e.getMessage());
        }
    }
}
