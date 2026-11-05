
package com.nys.nys_backend.controller;

import com.nys.nys_backend.entity.Competence;
import com.nys.nys_backend.service.CompetenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/competences")
public class CompetenceController {

    private final CompetenceService competenceService;

    public CompetenceController(CompetenceService competenceService) {
        this.competenceService = competenceService;
    }

    // Lister toutes les compétences
    @GetMapping
    public ResponseEntity<List<Competence>> listerCompetences() {
        return ResponseEntity.ok(
                competenceService.listerCompetences()
        );
    }

    // Consulter une compétence
    @GetMapping("/{id}")
    public ResponseEntity<Competence> obtenirCompetence(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                competenceService.obtenirCompetence(id)
        );
    }

    // Ajouter une compétence
    @PostMapping
    public ResponseEntity<Competence> creerCompetence(
            @Valid @RequestBody Competence competence) {

        Competence nouvelleCompetence =
                competenceService.creerCompetence(competence);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nouvelleCompetence);
    }

    // Modifier une compétence
    @PutMapping("/{id}")
    public ResponseEntity<Competence> modifierCompetence(
            @PathVariable Long id,
            @Valid @RequestBody Competence donnees) {

        Competence competenceModifiee =
                competenceService.modifierCompetence(id, donnees);

        return ResponseEntity.ok(competenceModifiee);
    }

    // Supprimer une compétence
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerCompetence(
            @PathVariable Long id) {

        competenceService.supprimerCompetence(id);

        return ResponseEntity.noContent().build();
    }
}
