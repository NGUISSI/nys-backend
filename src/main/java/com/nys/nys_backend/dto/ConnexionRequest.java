package com.nys.nys_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ConnexionRequest(

    @NotBlank(message = "L'adresse e-mail est obligatoire")
    @Email(message = "L'adresse e-mail est invalide")
    String email,

    @NotBlank(message = "Le mot de passe est obligatoire")
    String motDePasse

) {}
