package org.example.progetto_gestionale_cv_server.USER.service;

import org.example.progetto_gestionale_cv_server.USER.DTOs.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperUser;
import org.example.progetto_gestionale_cv_server.utility.generateToken.GenerateToken;
import org.springframework.stereotype.Service;

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

    @Override
    public void registrazioneUtente(RegistrazioneUtenteDTO datiUtente) throws RuntimeException {
        Users utente = this.mapperUser.FromDTOToEntity(datiUtente);
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
