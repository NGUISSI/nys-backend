package com.nys.nys_backend.service;

import com.nys.nys_backend.entity.Administrateur;
import com.nys.nys_backend.repository.AdministrateurRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdministrateurService {

    private final AdministrateurRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdministrateurService(
            AdministrateurRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Administrateur creerAdministrateur(
            String email,
            String motDePasse) {

        String emailNormalise = email.trim().toLowerCase();

        if (repository.findByEmail(emailNormalise).isPresent()) {
            throw new IllegalArgumentException(
                "Un compte existe déjà avec cette adresse e-mail."
            );
        }

        Administrateur administrateur = new Administrateur(
            emailNormalise,
            passwordEncoder.encode(motDePasse),
            "ADMIN"
        );

        return repository.save(administrateur);
    }
}
