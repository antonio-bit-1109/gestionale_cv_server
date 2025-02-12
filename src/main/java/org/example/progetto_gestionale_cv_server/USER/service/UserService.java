package org.example.progetto_gestionale_cv_server.USER.service;

import org.apache.catalina.User;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.repository.CredenzialiRepository;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperUser;
import org.example.progetto_gestionale_cv_server.utility.generateToken.GenerateToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final CredenzialiRepository credenzialiRepository;
    private final MapperUser mapperUser;
    private final GenerateToken generateToken;

    public UserService(UserRepository userRepo, MapperUser mapperuser, GenerateToken generateToken, CredenzialiRepository credenzialiRepository) {
        this.userRepository = userRepo;
        this.mapperUser = mapperuser;
        this.generateToken = generateToken;
        this.credenzialiRepository = credenzialiRepository;
    }


    // se i dati inviati rispettano certe credenziali viene creato un utente ADMIN altrimenti solo utenti USER
    // può essere creato solo 1 utente ADMIN per l'applicazione
    @Override
    public boolean registrazioneUtente(RegistrazioneUtenteDTO datiUtente) throws RuntimeException {

        if (this.mapperUser.isCreatingAnAdmin(datiUtente)) {
            return this.MapUtenteToEntity(datiUtente, true);
        } else {
            return this.MapUtenteToEntity(datiUtente, false);
        }

    }

    // mappa dto a utente e credenziali e li salva nel db
    private boolean MapUtenteToEntity(RegistrazioneUtenteDTO datiUtente, boolean isAdmin) {
        return this.mapperUser.FromDTOToEntity(datiUtente, isAdmin);

    }

    @Override
    public String doLogin(LoginDTO datiLogin) throws RuntimeException {

//        Optional<Users> utenteOptional = this.userRepository.findByEmail(datiLogin.getEmail());
        Optional<Credenziali> credenzialiOptional = this.credenzialiRepository.findByEmail(datiLogin.getEmail());

        if (credenzialiOptional.isEmpty()) {
            throw new RuntimeException("l'utente con le credenziali selezionate non esiste.");
        }

        Credenziali credenzialiUtente = credenzialiOptional.get();

        this.mapperUser.confirmPassword(credenzialiUtente, datiLogin.getPassword());

        Users utenteAssociato = credenzialiUtente.getUser();
        return this.generateToken.tokenGeneration(utenteAssociato);
    }
}
