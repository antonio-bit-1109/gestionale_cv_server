package org.example.progetto_gestionale_cv_server.utility.Mapper;

import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.repository.CredenzialiRepository;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.Edit_utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.DTOs.resp.Get_Utente_DTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.example.progetto_gestionale_cv_server.utility.configuration.ConfigurationConnectionProp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class MapperUser {

    private final BCryptPasswordEncoder passwordEncoder;
    private final CredenzialiRepository credenzialiRepository;
    private final UserRepository userRepository;
    private final ConfigurationConnectionProp configurationConnectionProp;

    //costruttore privato
    private MapperUser(BCryptPasswordEncoder passwordEncoder,
                       CredenzialiRepository credenzialiRepository,
                       UserRepository userRepository,
                       ConfigurationConnectionProp config_conn
    ) {
        this.passwordEncoder = passwordEncoder;
        this.credenzialiRepository = credenzialiRepository;
        this.userRepository = userRepository;
        this.configurationConnectionProp = config_conn;
    }


    public boolean FromDTOToEntity(RegistrazioneUtenteDTO dtoregistrazione, boolean creatingAdmin) {

        // String ImmagineDefault_profilo = Paths.get("").toAbsolutePath() + "/src/main/resources/static/images/default.jpg";
        String port = this.configurationConnectionProp.getServerPort();
        String address = this.configurationConnectionProp.getServerAddress();

        StringBuilder sb = new StringBuilder();
        sb.append("http://").append(address).append(":").append(port).append("/images").append("/get").append("/default.jpg");

        Users user = new Users();
        user.setNome(dtoregistrazione.getNome());
        user.setCognome(dtoregistrazione.getCognome());
        user.setTelefono(dtoregistrazione.getTelefono());
        user.setProfileImage(sb.toString());
        user.setConsensoTrattamentoDati(dtoregistrazione.getConsensoTrattamentoDati());

        Credenziali credenziali = new Credenziali();
        credenziali.setPassword(this.passwordEncoder.encode(dtoregistrazione.getPassword()));
        credenziali.setEmail(dtoregistrazione.getEmail());
        credenziali.setUser(user);

        if (creatingAdmin) {
            credenziali.setRole("ADMIN");
        } else {
            credenziali.setRole("USER");
        }

        user.setCredenziali(credenziali);
        this.userRepository.save(user);
        this.credenzialiRepository.save(credenziali);

        return creatingAdmin;

    }

    public Get_Utente_DTO FromEntityToDTO_get_user(Users utente, Credenziali credenzialiUtente) {
        Get_Utente_DTO utenteDto = new Get_Utente_DTO();

        utenteDto.setCognome(utente.getCognome());
        utenteDto.setNome(utente.getNome());
        utenteDto.setEmail(credenzialiUtente.getEmail());
        utenteDto.setImgProfilo(utente.getProfileImage());
        utenteDto.setTelefono(utente.getTelefono());
        utenteDto.setRuolo(credenzialiUtente.getRole());
        utenteDto.setId_utente(utente.getId());
        utenteDto.setActive(utente.getIsActive());

        return utenteDto;

    }

    public void confirmPassword(Credenziali credenzialiUtente, String passwordClient) {
        if (!(this.passwordEncoder.matches(passwordClient, credenzialiUtente.getPassword()))) {
            throw new RuntimeException("la password non coincide");
        }
    }

    // se dalla DTO mi arrivano specifici dati, da quei dati vado creando un ADMIN e non uno USER classico
    public boolean isCreatingAnAdmin(RegistrazioneUtenteDTO datiRegistrazione) {

        if (!datiRegistrazione.getNome().equals("performanceAdmin")) {
            return false;
        }

        if (!datiRegistrazione.getCognome().equals("performanceAdmin")) {
            return false;
        }

        if (!datiRegistrazione.getEmail().equals("admin@gmail.com")) {
            return false;
        }

        if (!datiRegistrazione.getTelefono().equals("+39 111 1111111")) {
            return false;
        }

        if (!datiRegistrazione.getPassword().equals("Ar11091995!.!")) {
            return false;
        }

        return true;
    }

    public void editingUserData(Edit_utente_DTO datiEdit, Users utente, Credenziali credenzialiUtente) {

        if (!(utente.getNome().equals(datiEdit.getNome()))) {
            utente.setNome(datiEdit.getNome());
        }

        if (!(utente.getCognome().equals(datiEdit.getCognome()))) {
            utente.setCognome(datiEdit.getCognome());
        }

        if (!(utente.getTelefono().equals(datiEdit.getTelefono()))) {
            utente.setTelefono(datiEdit.getTelefono());
        }

        if (!(credenzialiUtente.getEmail().equals(datiEdit.getEmail()))) {
            credenzialiUtente.setEmail(datiEdit.getEmail());
        }

        if (!(this.passwordEncoder.matches(datiEdit.getPassword(), credenzialiUtente.getPassword()))) {
            credenzialiUtente.setPassword(this.passwordEncoder.encode(datiEdit.getPassword()));
        }

        this.userRepository.save(utente);
        this.credenzialiRepository.save(credenzialiUtente);
    }
}
