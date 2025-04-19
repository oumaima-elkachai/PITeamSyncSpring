package com.example.payroll.controllers;

import com.example.payroll.model.Projet;
import com.example.payroll.repository.ProjetRepository;
import com.example.payroll.services.interfaces.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    //***
    @Autowired
    private ProjetRepository projetRepository;
    @GetMapping("/{id}/nom")
    public ResponseEntity<String> getNomProjetById(@PathVariable String id) {
        Optional<Projet> projet = projetRepository.findById(id);
        return projet.map(p -> ResponseEntity.ok(p.getName()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //***
    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        List<Projet> projets = projetService.getAllProjets();  // Service qui récupère les projets
        if (projets.isEmpty()) {
            return ResponseEntity.noContent().build();  // Si aucun projet, retourne un code 204 No Content
        }
        return ResponseEntity.ok(projets);  // Si projets trouvés, retourne un code 200 OK avec les projets
    }

    @GetMapping("/{id}")
    public Optional<Projet> getProjetById(@PathVariable String id) {
        return projetService.getProjetById(id);
    }

    @PostMapping
    public Projet createProjet(@RequestBody Projet projet) {
        return projetService.createProjet(projet);
    }

    @PutMapping("/{id}")
    public Projet updateProjet(@PathVariable String id, @RequestBody Projet projet) {
        return projetService.updateProjet(id, projet);
    }

    @DeleteMapping("/{id}")
    public void deleteProjet(@PathVariable String id) {
        projetService.deleteProjet(id);
    }
}
