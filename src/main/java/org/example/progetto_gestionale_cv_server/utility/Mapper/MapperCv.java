package org.example.progetto_gestionale_cv_server.utility.Mapper;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.resp.Cv_get_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.utility.UTILITYPDF.GenerazionePDF;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class MapperCv {

    private CvRepository cvRepository;
    private GenerazionePDF generazionePDF;

    //costr
    public MapperCv(CvRepository cvRepository, GenerazionePDF generazionePDF) {
        this.cvRepository = cvRepository;
        this.generazionePDF = generazionePDF;
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
        return cvDTO;
    }

    public void ModificaCv(DatiCreazionePDF_DTO datipdf, Users users) throws RuntimeException, IOException {

        LocalDateTime dataCorrente = LocalDateTime.now();
        Timestamp oraCorrente = Timestamp.valueOf(dataCorrente);

        for (CVs cv : users.getListaCv()) {

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
//            this.cvRepository.save(cv);
        }


    }
}
