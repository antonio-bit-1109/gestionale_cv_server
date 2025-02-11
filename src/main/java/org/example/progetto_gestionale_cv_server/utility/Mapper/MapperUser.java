package org.example.progetto_gestionale_cv_server.utility.Mapper;

import org.example.progetto_gestionale_cv_server.USER.DTOs.req.RegistrazioneUtenteDTO;
import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MapperUser {

    private final BCryptPasswordEncoder passwordEncoder;

    //costruttore privato
    private MapperUser(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public Users FromDTOToEntity(RegistrazioneUtenteDTO dtoregistrazione, boolean creatingAdmin) {
        Users user = new Users();
        user.setNome(dtoregistrazione.getNome());
        user.setCognome(dtoregistrazione.getCognome());
        user.setEmail(dtoregistrazione.getEmail());
        user.setPassword(this.passwordEncoder.encode(dtoregistrazione.getPassword()));
        user.setTelefono(dtoregistrazione.getTelefono());
        user.setConsensoTrattamentoDati(dtoregistrazione.getConsensoTrattamentoDati());

        if (creatingAdmin) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }
        
        return user;
    }

    public void confirmPassword(Users utente, String passwordClient) {
        if (!(this.passwordEncoder.matches(passwordClient, utente.getPassword()))) {
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
