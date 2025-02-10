package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperCv;
import org.example.progetto_gestionale_cv_server.utility.generazionePDF.GenerazionePDF;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CvService implements ICvService {

    private final CvRepository cvRepository;
    private final UserRepository userRepository;
    private final MapperCv mapperCv;
    private final GenerazionePDF generazionePDF;

    public CvService(CvRepository cvRepo, UserRepository userRepository, MapperCv mapperCv, GenerazionePDF generazionePDF) {
        this.cvRepository = cvRepo;
        this.userRepository = userRepository;
        this.mapperCv = mapperCv;
        this.generazionePDF = generazionePDF;
    }

    //metodo di creazione del pdf e popolamento tabella cv
    @Override
    public void creaPDF_Record_CV(DatiCreazionePDF_DTO datiCreazionePDFDto) throws RuntimeException, IOException {

        CVs cv = this.mapperCv.FromDTOToEntity(datiCreazionePDFDto);
        Users utente = returnUserIfExist(datiCreazionePDFDto);


        cv.setUser(utente); // Set the user before saving the CV
        CVs cvSaved = this.cvRepository.save(cv);
        utente.getListaCv().add(cvSaved); // Add the CV to the user's list of CVs
        this.userRepository.save(utente);

        this.generazionePDF.CreazionePDFFileSystem(utente, cvSaved, false);
//        cvSaved.setNome_file_pdf(this.generazionePDF.getPath(utente, cv));
        this.cvRepository.save(cv);
    }


    // metodo che dovrà modificare i campi cv della tabella e sostituire il pdf con i dati aggiornati.
    @Override
    public void modificaPDF_Record_CV(DatiCreazionePDF_DTO datiModificaPDF) throws IOException {

        Users user = returnUserIfExist(datiModificaPDF);
        this.mapperCv.ModificaCv(datiModificaPDF, user);

    }

    //privato
    // ritornare lo user, se esiste, come una entity
    private Users returnUserIfExist(DatiCreazionePDF_DTO datiModificaPDF) throws RuntimeException {
        Optional<Users> utenteOpt = this.userRepository.findById(datiModificaPDF.getIdUtente());

        if (utenteOpt.isEmpty()) {
            throw new RuntimeException("L'utente non esiste.");
        }

        return utenteOpt.get();
    }

}
