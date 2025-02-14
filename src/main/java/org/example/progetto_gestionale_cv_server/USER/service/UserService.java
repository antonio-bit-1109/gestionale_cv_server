package org.example.progetto_gestionale_cv_server.USER.service;

import org.apache.catalina.User;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.repository.CredenzialiRepository;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.CambioImgProfilo_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperUser;
import org.example.progetto_gestionale_cv_server.utility.generateToken.GenerateToken;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final CredenzialiRepository credenzialiRepository;
    private final MapperUser mapperUser;
    private final GenerateToken generateToken;
    private final PathMatcher pathMatcher;

    public UserService(UserRepository userRepo, MapperUser mapperuser, GenerateToken generateToken, CredenzialiRepository credenzialiRepository, PathMatcher pathMatcher) {
        this.userRepository = userRepo;
        this.mapperUser = mapperuser;
        this.generateToken = generateToken;
        this.credenzialiRepository = credenzialiRepository;
        this.pathMatcher = pathMatcher;
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

    @Override
    public boolean cambioImgProfilo(MultipartFile fileImg, Long idUser) throws IOException {
        Users utente = this.returnUserIfExist(idUser);

        Path destinazioneSalvataggioFoto = Paths.get("").toAbsolutePath().resolve("src/main/resources/static/images");

        try {
            Path filePath = destinazioneSalvataggioFoto.resolve(Objects.requireNonNull(fileImg.getOriginalFilename()));
            Files.copy(fileImg.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            utente.setProfileImage(filePath.toString());
            this.userRepository.save(utente);
            return true;
        } catch (IOException ex) {
            throw new IOException("errore durante il caricamento dell'imagine profilo:" + ex.getMessage());
        }
    }


    // mappa dto a utente e credenziali e li salva nel db
    public boolean MapUtenteToEntity(RegistrazioneUtenteDTO datiUtente, boolean isAdmin) {
        return this.mapperUser.FromDTOToEntity(datiUtente, isAdmin);

    }

    @Override
    public String doLogin(LoginDTO datiLogin) throws RuntimeException {

        Optional<Credenziali> credenzialiOptional = this.credenzialiRepository.findByEmail(datiLogin.getEmail());

        if (credenzialiOptional.isEmpty()) {
            throw new RuntimeException("l'utente con le credenziali selezionate non esiste.");
        }

        Credenziali credenzialiUtente = credenzialiOptional.get();

        this.mapperUser.confirmPassword(credenzialiUtente, datiLogin.getPassword());

        Users utenteAssociato = credenzialiUtente.getUser();
        return this.generateToken.tokenGeneration(utenteAssociato);
    }

    //privato
    // ritornare lo user, se esiste, come una entity
    public Users returnUserIfExist(Long id_utente) throws RuntimeException {
        Optional<Users> utenteOpt = this.userRepository.findById(id_utente);

        if (utenteOpt.isEmpty()) {
            throw new RuntimeException("L'utente non esiste.");
        }

        return utenteOpt.get();
    }
}
