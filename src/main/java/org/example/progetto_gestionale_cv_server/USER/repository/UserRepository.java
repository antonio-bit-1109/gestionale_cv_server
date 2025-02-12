package org.example.progetto_gestionale_cv_server.USER.repository;

import org.example.progetto_gestionale_cv_server.USER.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

}
