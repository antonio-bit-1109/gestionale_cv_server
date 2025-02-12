package org.example.progetto_gestionale_cv_server.utility.Mapper;

import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.example.progetto_gestionale_cv_server.CREDENZIALI.repository.CredenzialiRepository;
import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.example.progetto_gestionale_cv_server.USER.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;

@Component
public class MapperUser {

    private final BCryptPasswordEncoder passwordEncoder;
    private final CredenzialiRepository credenzialiRepository;
    private final UserRepository userRepository;

    //costruttore privato
    private MapperUser(BCryptPasswordEncoder passwordEncoder, CredenzialiRepository credenzialiRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.credenzialiRepository = credenzialiRepository;
        this.userRepository = userRepository;
    }


    public boolean FromDTOToEntity(RegistrazioneUtenteDTO dtoregistrazione, boolean creatingAdmin) {

        String ImmagineDefault_profilo = Paths.get("").toAbsolutePath() + "/src/main/resources/static/images/default.jpg";


        Users user = new Users();
        user.setNome(dtoregistrazione.getNome());
        user.setCognome(dtoregistrazione.getCognome());
        user.setTelefono(dtoregistrazione.getTelefono());
        user.setProfileImage(ImmagineDefault_profilo);
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
}
