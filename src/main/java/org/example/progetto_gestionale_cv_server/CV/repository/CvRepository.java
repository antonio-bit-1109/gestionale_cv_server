package org.example.progetto_gestionale_cv_server.CV.repository;

import org.example.progetto_gestionale_cv_server.CV.entity.CVs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CvRepository extends JpaRepository<CVs, Long> {
    List<CVs> findByCompetenzeContainingIgnoreCase(String competenze);


//    @Query("")

}
