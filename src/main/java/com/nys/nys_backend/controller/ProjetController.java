
package com.nys.nys_backend.controller;

import com.nys.nys_backend.entity.Projet;
import com.nys.nys_backend.service.ProjetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/projets")
public class ProjetController {

    private final ProjetService projetService;

    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    // Lister tous les projets
    @GetMapping
    public ResponseEntity<List<Projet>> listerProjets() {
        return ResponseEntity.ok(projetService.listerProjets());
    }

    // Consulter un projet
    @GetMapping("/{id}")
    public ResponseEntity<Projet> obtenirProjet(@PathVariable Long id) {
        return ResponseEntity.ok(projetService.obtenirProjet(id));
    }

    // Ajouter un projet
    @PostMapping
    public ResponseEntity<Projet> creerProjet(
            @Valid @RequestBody Projet projet) {

        Projet nouveauProjet = projetService.creerProjet(projet);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nouveauProjet);
    }

    // Modifier un projet
    @PutMapping("/{id}")
    public ResponseEntity<Projet> modifierProjet(
            @PathVariable Long id,
            @Valid @RequestBody Projet donnees) {

        Projet projetModifie =
                projetService.modifierProjet(id, donnees);

        return ResponseEntity.ok(projetModifie);
    }

    // Supprimer un projet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProjet(@PathVariable Long id) {
        projetService.supprimerProjet(id);
        return ResponseEntity.noContent().build();
    }
}
