package org.example.progetto_gestionale_cv_server.utility.Mapper;

import org.example.progetto_gestionale_cv_server.CV.DTOs.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MapperCv {

    public CVs FromDTOToEntity(DatiCreazionePDF_DTO datiPdf) {

        LocalDateTime dataCorrente = LocalDateTime.now();
        Timestamp oraCorrente = Timestamp.valueOf(dataCorrente);
        CVs cvEntity = new CVs();
        cvEntity.setTitolo(datiPdf.getTitolo());
        cvEntity.setCompetenze(datiPdf.getCompetenze());
        cvEntity.setIstruzione(datiPdf.getIstruzione());
        cvEntity.setEsperienze_Precedenti(datiPdf.getEsperienzePrecedenti());
        cvEntity.setCreated_at(oraCorrente);
        return cvEntity;
    }
}
