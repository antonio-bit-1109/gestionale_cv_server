package org.example.progetto_gestionale_cv_server.CREDENZIALI.repository;

import org.example.progetto_gestionale_cv_server.CREDENZIALI.entity.Credenziali;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Repository
public interface CredenzialiRepository extends JpaRepository<Credenziali, Long> {

    List<Credenziali> findByRole(String role);

    Optional<Credenziali> findByEmail(String email);
}
