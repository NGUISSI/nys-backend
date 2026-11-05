
package com.nys.nys_backend.service;

import com.nys.nys_backend.entity.Competence;
import com.nys.nys_backend.repository.CompetenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompetenceService {

    private final CompetenceRepository competenceRepository;

    public CompetenceService(CompetenceRepository competenceRepository) {
        this.competenceRepository = competenceRepository;
    }

    @Transactional(readOnly = true)
    public List<Competence> listerCompetences() {
        return competenceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Competence obtenirCompetence(Long id) {
        return competenceRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Compétence introuvable : " + id
                        )
                );
    }

    public Competence creerCompetence(Competence competence) {
        competence.setNom(verifierNom(competence.getNom()));
        competence.setCategorie(nettoyer(competence.getCategorie()));
        competence.setNiveau(nettoyer(competence.getNiveau()));

        return competenceRepository.save(competence);
    }

    public Competence modifierCompetence(
            Long id,
            Competence donnees) {

        Competence competence = obtenirCompetence(id);

        competence.setNom(verifierNom(donnees.getNom()));
        competence.setCategorie(nettoyer(donnees.getCategorie()));
        competence.setNiveau(nettoyer(donnees.getNiveau()));

        return competenceRepository.save(competence);
    }

    public void supprimerCompetence(Long id) {
        Competence competence = obtenirCompetence(id);
        competenceRepository.delete(competence);
    }

    private String verifierNom(String nom) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom de la compétence est obligatoire"
            );
        }

        nom = nom.trim();

        if (nom.length() > 100) {
            throw new IllegalArgumentException(
                    "Le nom ne doit pas dépasser 100 caractères"
            );
        }

        return nom;
    }

    private String nettoyer(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return null;
        }

        return valeur.trim();
    }
}
