
package com.nys.nys_backend.service;

import com.nys.nys_backend.entity.Projet;
import com.nys.nys_backend.repository.ProjetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjetService {

    private final ProjetRepository projetRepository;

    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    @Transactional(readOnly = true)
    public List<Projet> listerProjets() {
        return projetRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Projet obtenirProjet(Long id) {
        return projetRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Projet introuvable avec l'identifiant : " + id
                        )
                );
    }

    public Projet creerProjet(Projet projet) {
        projet.setTitre(verifierTitre(projet.getTitre()));

        projet.setCategorie(nettoyer(projet.getCategorie()));
        projet.setDescription(nettoyer(projet.getDescription()));
        projet.setTechnologies(nettoyer(projet.getTechnologies()));
        projet.setDetails(nettoyer(projet.getDetails()));
        projet.setFonctionnalites(nettoyer(projet.getFonctionnalites()));
        projet.setGithub(nettoyer(projet.getGithub()));

        return projetRepository.save(projet);
    }

    public Projet modifierProjet(Long id, Projet donnees) {
        Projet projet = obtenirProjet(id);

        projet.setTitre(verifierTitre(donnees.getTitre()));
        projet.setCategorie(nettoyer(donnees.getCategorie()));
        projet.setDescription(nettoyer(donnees.getDescription()));
        projet.setTechnologies(nettoyer(donnees.getTechnologies()));
        projet.setDetails(nettoyer(donnees.getDetails()));
        projet.setFonctionnalites(nettoyer(donnees.getFonctionnalites()));
        projet.setGithub(nettoyer(donnees.getGithub()));

        return projetRepository.save(projet);
    }

    public void supprimerProjet(Long id) {
        Projet projet = obtenirProjet(id);
        projetRepository.delete(projet);
    }

    private String verifierTitre(String titre) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException(
                    "Le titre du projet est obligatoire"
            );
        }

        titre = titre.trim();

        if (titre.length() > 150) {
            throw new IllegalArgumentException(
                    "Le titre ne doit pas dépasser 150 caractères"
            );
        }

        return titre;
    }

    private String nettoyer(String valeur) {
        if (valeur == null || valeur.isBlank()) {
            return null;
        }

        return valeur.trim();
    }
}
