package com.nys.nys_backend.repository;

import com.nys.nys_backend.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministrateurRepository
        extends JpaRepository<Administrateur, Long> {

    Optional<Administrateur> findByEmail(String email);
}
