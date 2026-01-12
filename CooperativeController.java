package com.coopagri.controllers;

import com.coopagri.models.Cooperative;
import com.coopagri.services.CooperativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cooperatives")
@CrossOrigin(origins = "*")  // Pour votre frontend
public class CooperativeController {
    
    @Autowired
    private CooperativeService cooperativeService;
    
    // Récupérer toutes les coopératives
    @GetMapping
    public ResponseEntity<List<Cooperative>> getAllCooperatives() {
        return ResponseEntity.ok(cooperativeService.findAll());
    }
    
    // Récupérer une coopérative par type
    @GetMapping("/{type}")
    public ResponseEntity<Cooperative> getCooperative(@PathVariable String type) {
        return ResponseEntity.ok(cooperativeService.findByType(type));
    }
    
    // Ajouter une nouvelle récolte
    @PostMapping("/{type}/harvests")
    public ResponseEntity<?> addHarvest(
            @PathVariable String type,
            @RequestBody HarvestRequest request) {
        
        cooperativeService.addHarvest(type, request);
        return ResponseEntity.ok("Récolte enregistrée avec succès");
    }
    
    // Générer un rapport PDF avec Java
    @GetMapping("/{type}/report")
    public ResponseEntity<byte[]> generateReport(@PathVariable String type) {
        byte[] pdfReport = cooperativeService.generatePDFReport(type);
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(pdfReport);
    }
}