package org.example.progetto_gestionale_cv_server.CV.service;

import org.example.progetto_gestionale_cv_server.CV.DTOs.BaseDTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.ID_UTENTE_CV_DTO;
import org.example.progetto_gestionale_cv_server.CV.DTOs.req.DatiCreazionePDF_DTO;
import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.example.progetto_gestionale_cv_server.CV.repository.CvRepository;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperCv;
import org.example.progetto_gestionale_cv_server.utility.generazionePDF.GenerazionePDF;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
        Users utente = returnUserIfExist(datiCreazionePDFDto.getIdUtente());


        cv.setUser(utente); // Set the user before saving the CV
        CVs cvSaved = this.cvRepository.save(cv);
        utente.getListaCv().add(cvSaved); // Add the CV to the user's list of CVs
        this.userRepository.save(utente);

        this.generazionePDF.CreazionePDFFileSystem(utente, cvSaved, false);

    }


    // metodo che dovrà modificare i campi cv della tabella e sostituire il pdf con i dati aggiornati.
    @Override
    public void modificaPDF_Record_CV(DatiCreazionePDF_DTO datiModificaPDF) throws IOException {
        Users user = returnUserIfExist(datiModificaPDF.getIdUtente());
        this.mapperCv.ModificaCv(datiModificaPDF, user);
    }

    @Override
    public void CancellaCV(ID_UTENTE_CV_DTO ids_utente_cv) throws IOException {

        CVs cv = this.returnCvIfExist(ids_utente_cv.getId_cv());
        Users utente = this.returnUserIfExist(ids_utente_cv.getId_utente());

        utente.getListaCv().forEach(cVs -> {

            if (cVs.getId().equals(cv.getId())) {
                try {
                    this.generazionePDF.CancellaPDF_file_System(cv.getNome_file_pdf());
                } catch (IOException e) {
                    throw new RuntimeException("errore durante la cancellazione dal file system del file pdf:" + e.getMessage());
                }
                this.cvRepository.delete(cv);

            }
        });


    }

    @Override
    public BaseDTO getCv(ID_UTENTE_CV_DTO dati_id) {
        Users utente = this.returnUserIfExist(dati_id.getId_utente());

        for (CVs cV : utente.getListaCv()) {
            if (cV.getId().equals(dati_id.getId_cv())) {
                return this.mapperCv.fromEntityToDTO(cV);
            }
        }

        throw new RuntimeException("impossibile trovare il cv specificato");

    }

    @Override
    public List<BaseDTO> getAll_CV(Long id_utente) {
        Users user = this.returnUserIfExist(id_utente);

        List<BaseDTO> listaCV = new ArrayList<>();

        for (CVs cv : user.getListaCv()) {
            BaseDTO cvDTO = this.mapperCv.fromEntityToDTO(cv);
            listaCV.add(cvDTO);
        }

        return listaCV;
    }


    //privato
    // ritornare lo user, se esiste, come una entity
    private Users returnUserIfExist(Long id_utente) throws RuntimeException {
        Optional<Users> utenteOpt = this.userRepository.findById(id_utente);

        if (utenteOpt.isEmpty()) {
            throw new RuntimeException("L'utente non esiste.");
        }

        return utenteOpt.get();
    }


    // metodo che ritorna un cv se esiste altrimenti lancia errore
    private CVs returnCvIfExist(Long id_cv) {
        Optional<CVs> cvOpt = this.cvRepository.findById(id_cv);

        if (cvOpt.isEmpty()) {
            throw new RuntimeException("cv non trovato.");
        }

        return cvOpt.get();
    }
}
