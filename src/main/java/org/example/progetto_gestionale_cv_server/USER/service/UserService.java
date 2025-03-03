package org.example.progetto_gestionale_cv_server.USER.service;

import org.apache.catalina.User;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.repository.CredenzialiRepository;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.CambioImgProfilo_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.Edit_utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.LoginDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_List_utenti_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_Utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.utility.Mapper.MapperUser;
import org.example.progetto_gestionale_cv_server.utility.configuration.ConfigurationConnectionProp;
import org.example.progetto_gestionale_cv_server.utility.customExceptions.EmailAlreadyUsed;
import org.example.progetto_gestionale_cv_server.utility.generateToken.GenerateToken;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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
    private final ConfigurationConnectionProp configurationConnectionProp;
    //  private final PathMatcher pathMatcher;

    public UserService(ConfigurationConnectionProp connectionProp, UserRepository userRepo, MapperUser mapperuser, GenerateToken generateToken, CredenzialiRepository credenzialiRepository) {
        this.userRepository = userRepo;
        this.mapperUser = mapperuser;
        this.generateToken = generateToken;
        this.credenzialiRepository = credenzialiRepository;
        this.configurationConnectionProp = connectionProp;
//        this.pathMatcher = pathMatcher;
    }


    // se i dati inviati rispettano certe credenziali viene creato un utente ADMIN altrimenti solo utenti USER
    // può essere creato solo 1 utente ADMIN per l'applicazione
    @Override
    public boolean registrazioneUtente(RegistrazioneUtenteDTO datiUtente) throws RuntimeException {

        Optional<Credenziali> credenzialiOpt = this.credenzialiRepository.findByEmail(datiUtente.getEmail());

        if (credenzialiOpt.isPresent()) {
            throw new EmailAlreadyUsed("questa mail non è disponibile. Provane con una diversa", "email");
        }

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

            String address = this.configurationConnectionProp.getServerAddress();
            String port = this.configurationConnectionProp.getServerPort();

            StringBuilder sb = new StringBuilder();
            sb.append("http://")
                    .append(address)
                    .append(":")
                    .append(port)
                    .append("/images")
                    .append("/get")
                    .append("/")
                    .append(Objects.requireNonNull(fileImg.getOriginalFilename()).replace(" ", "_"));

            Path filePath = destinazioneSalvataggioFoto.resolve(Objects.requireNonNull(fileImg.getOriginalFilename().replace(" ", "_")));
            Files.copy(fileImg.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            utente.setProfileImage(sb.toString());
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

    public List<Users> returnAllUsers() {
        return this.userRepository.findAll();
    }

    public Credenziali returnCredenzialiIfExists(Users utente) {

        String email = utente.getCredenziali().getEmail();

        if (email == null) {
            throw new NullPointerException("nessuna mail disponibile per l'utente selezionato.");
        }

        Optional<Credenziali> credenzialiOpt = this.credenzialiRepository.findByEmail(email);

        if (credenzialiOpt.isEmpty()) {
            throw new RuntimeException("impossibile ricavare le credenziali per l'email fornita.");
        }

        return credenzialiOpt.get();
    }

    @Override
    public Get_Utente_DTO GetUtenteSingolo(Long id_utente) {
        Users utente = this.returnUserIfExist(id_utente);
        Credenziali credenzialiUtente = this.returnCredenzialiIfExists(utente);
        return this.mapperUser.FromEntityToDTO_get_user(utente, credenzialiUtente);
    }

    @Override
    public List<Get_Utente_DTO> getListaUtenti() {

        List<Get_Utente_DTO> listaUtenti = new ArrayList<>();

        this.returnAllUsers().forEach(users -> {

            Credenziali credenzialiUtente = this.returnCredenzialiIfExists(users);
            Get_Utente_DTO utenteDto = this.mapperUser.FromEntityToDTO_get_user(users, credenzialiUtente);
            listaUtenti.add(utenteDto);
        });
        return listaUtenti;
    }

    @Override
    public boolean editUtente(Edit_utente_DTO datiEdit, Long id_utente) {
        Users utente = this.returnUserIfExist(id_utente);
        Credenziali credenzialiUtente = this.returnCredenzialiIfExists(utente);

        this.mapperUser.editingUserData(datiEdit, utente, credenzialiUtente);
        return true;
    }

    @Override
    public boolean handleStatus(Long id_utente) {

        Users utente = this.returnUserIfExist(id_utente);
        utente.setActive(!utente.getIsActive());
        userRepository.save(utente);
        return utente.getIsActive();

    }

    @Override
    public String getProfileImage(Long id_utente) {
        Users user = this.returnUserIfExist(id_utente);
        return user.getProfileImage();
    }
}
