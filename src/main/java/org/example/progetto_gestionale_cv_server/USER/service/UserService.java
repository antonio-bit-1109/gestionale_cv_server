package org.example.progetto_gestionale_cv_server.USER.service;

import org.apache.catalina.User;
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
    private final MapperUser mapperUser;
    private final GenerateToken generateToken;

    public UserService(UserRepository userRepo, MapperUser mapperuser, GenerateToken generateToken) {
        this.userRepository = userRepo;
        this.mapperUser = mapperuser;
        this.generateToken = generateToken;
    }


    // se i dati inviati rispettano certe credenziali viene creato un utente ADMIN altrimenti solo utenti USER
    // può essere creato solo 1 utente ADMIN per l'applicazione
    @Override
    public void registrazioneUtente(RegistrazioneUtenteDTO datiUtente) throws RuntimeException {

        // controllo aggiuntivo per garantire un solo utente ADMIN
        // non dovrebbe essere necessario in quanto già la email è unique
        // quindi garantisce che solo un utente abbia la email giusta per poter essere registrato come admin
        List<Users> listaAdmin = this.userRepository.findByRole("ADMIN");
        if (listaAdmin.size() == 1) {
            throw new RuntimeException("non è possibile avere più di un profilo ADMIN");
        }

        if (this.mapperUser.isCreatingAnAdmin(datiUtente)) {
            this.MapUtenteToEntity(datiUtente, true);
        } else {
            this.MapUtenteToEntity(datiUtente, false);
        }

    }

    private void MapUtenteToEntity(RegistrazioneUtenteDTO datiUtente, boolean isAdmin) {
        Users utente = this.mapperUser.FromDTOToEntity(datiUtente, isAdmin);
        this.userRepository.save(utente);
    }

    @Override
    public String doLogin(LoginDTO datiLogin) throws RuntimeException {

        Optional<Users> utenteOptional = this.userRepository.findByEmail(datiLogin.getEmail());

        if (utenteOptional.isEmpty()) {
            throw new RuntimeException("l'utente non esiste.");
        }

        Users utente = utenteOptional.get();
        this.mapperUser.confirmPassword(utente, datiLogin.getPassword());
        return this.generateToken.tokenGeneration(utente);
    }
}
