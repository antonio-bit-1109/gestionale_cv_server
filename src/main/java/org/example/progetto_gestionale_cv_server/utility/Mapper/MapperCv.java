package org.example.progetto_gestionale_cv_server.utility.Mapper;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiModifica_cv_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Cv_get_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.CV.service.CvService;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.USER.service.UserService;
import org.example.progetto_gestionale_cv_server.utility.UTILITYPDF.GenerazionePDF;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Component
public class MapperCv {

    private final CvRepository cvRepository;
    private final GenerazionePDF generazionePDF;
    private final UserService userService;
    private final UserRepository userRepository;


    //costr
    public MapperCv(CvRepository cvRepository, GenerazionePDF generazionePDF, UserService userService, UserRepository userRepository) {
        this.cvRepository = cvRepository;
        this.generazionePDF = generazionePDF;
        this.userService = userService;
        this.userRepository = userRepository;

    }

    public CVs FromDTOToEntity(DatiCreazionePDF_DTO datiPdf) {

        LocalDateTime dataCorrente = LocalDateTime.now();
        Timestamp oraCorrente = Timestamp.valueOf(dataCorrente);
        CVs cvEntity = new CVs();
        cvEntity.setTitolo(datiPdf.getTitolo());
        cvEntity.setCompetenze(datiPdf.getCompetenze());
        cvEntity.setIstruzione(datiPdf.getIstruzione());
        cvEntity.setEsperienze_Precedenti(datiPdf.getEsperienzePrecedenti());
        cvEntity.setLingueConosciute(datiPdf.getLingueConosciute());
        cvEntity.setDescrizioneGenerale(datiPdf.getDescrizioneGenerale());
        cvEntity.setCreated_at(oraCorrente);
        return cvEntity;
    }

    // data l'entity cv ritorna una dto ad hoc per la get del singolo cv
    public BaseDTO fromEntityToDTO(CVs cv) {
        Cv_get_DTO cvDTO = new Cv_get_DTO();
        cvDTO.setId_cv(cv.getId());
        cvDTO.setPath_file_System_pdf(cv.getNome_file_pdf());
        cvDTO.setCompetenze(cv.getCompetenze());
        cvDTO.setCreated_at(cv.getCreated_at());
        cvDTO.setDescrizioneGenerale(cv.getDescrizioneGenerale());
        cvDTO.setIstruzione(cv.getIstruzione());
        cvDTO.setLingueConosciute(cv.getLingueConosciute());
        cvDTO.setUpdated_at(cv.getUpdated_at());
        cvDTO.setTitolo(cv.getTitolo());
        cvDTO.setEsperienze_Precedenti(cv.getEsperienze_Precedenti());
        cvDTO.setProprietarioCV(cv.getUser().getNome() + " " + cv.getUser().getCognome());
        cvDTO.setEmail(cv.getUser().getCredenziali().getEmail());
        cvDTO.setTelefono(cv.getUser().getTelefono());
        return cvDTO;
    }

    public void ModificaCv(DatiModifica_cv_DTO datipdf, Users users) throws RuntimeException, IOException {

        LocalDateTime dataCorrente = LocalDateTime.now();
        Timestamp oraCorrente = Timestamp.valueOf(dataCorrente);

//        CVs cv = this.cvService.returnCvIfExist(datipdf.getIdCv());
        Optional<CVs> cVsOptional = this.cvRepository.findById(datipdf.getIdCv());

        if (cVsOptional.isEmpty()) {
            throw new RuntimeException("il cv selezionato non esiste.");
        }

        CVs cv = cVsOptional.get();

        if (!(cv.getUser().getId().equals(datipdf.getIdUtente()))) {
            throw new RuntimeException("L'utente che sta cercando di modificare il curriculum non è il possessore del curriculum");
        }

        cv.setUpdated_at(oraCorrente);

        if (!(datipdf.getCompetenze().equalsIgnoreCase(cv.getCompetenze()))) {
            cv.setCompetenze(datipdf.getCompetenze());
        }

        if (!(datipdf.getIstruzione().equalsIgnoreCase(cv.getIstruzione()))) {
            cv.setIstruzione(datipdf.getIstruzione());
        }

        if (!(datipdf.getTitolo().equalsIgnoreCase(cv.getTitolo()))) {
            cv.setTitolo(datipdf.getTitolo());
        }

        if (!(datipdf.getDescrizioneGenerale().equalsIgnoreCase(cv.getDescrizioneGenerale()))) {
            cv.setDescrizioneGenerale(datipdf.getDescrizioneGenerale());
        }

        if (!(datipdf.getLingueConosciute().equalsIgnoreCase(cv.getLingueConosciute()))) {
            cv.setLingueConosciute(datipdf.getLingueConosciute());
        }

        if (!(datipdf.getEsperienzePrecedenti().equalsIgnoreCase(cv.getEsperienze_Precedenti()))) {
            cv.setEsperienze_Precedenti(datipdf.getEsperienzePrecedenti());
        }

        this.generazionePDF.CreazionePDFFileSystem(users, cv, true);

    }

    public void createCv(HashMap<String, String> mappaParti, MultipartFile file, String percorsoFileSuServer) {

        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(currentTime);
        CVs cv = new CVs();

        cv.setNome_file_pdf(file.getOriginalFilename());
        cv.setLingueConosciute(mappaParti.get("lingue_conosciute"));
        cv.setTitolo(mappaParti.get("titolo"));
        cv.setCreated_at(currentTimestamp);
        cv.setIstruzione(mappaParti.get("istruzione"));
        cv.setEsperienze_Precedenti(mappaParti.get("esperienze_precedenti"));
        cv.setCompetenze(mappaParti.get("competenze"));
        cv.setDescrizioneGenerale(mappaParti.get("descrizione_generale"));

        Users utente = this.userService.returnUserIfExist(Long.parseLong(mappaParti.get("id_utente")));
        utente.getListaCv().add(cv);
        cv.setUser(utente);

        this.cvRepository.save(cv);
        this.userRepository.save(utente);
    }
}
