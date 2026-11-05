package com.nys.nys_backend.config;

import com.nys.nys_backend.entity.Administrateur;
import com.nys.nys_backend.repository.AdministrateurRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdministrateurInitializer {

    @Bean
    CommandLineRunner initialiserAdministrateur(
            AdministrateurRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String email,
            @Value("${app.admin.password:}") String motDePasse) {

        return args -> {
            if (email.isBlank() || motDePasse.isBlank()) {
                System.out.println(
                    "Compte administrateur non initialisé : " +
                    "variables app.admin.email et app.admin.password absentes."
                );
                return;
            }

            String emailNormalise = email.trim().toLowerCase();

            if (repository.findByEmail(emailNormalise).isPresent()) {
                System.out.println(
                    "Le compte administrateur configuré existe déjà."
                );
                return;
            }

            Administrateur administrateur = new Administrateur(
                emailNormalise,
                passwordEncoder.encode(motDePasse),
                "ADMIN"
            );

            repository.save(administrateur);

            System.out.println(
                "Compte administrateur initialisé."
            );
        };
    }
}
