package com.coopagri.controller;

import com.coopagri.model.Cooperative;
import com.coopagri.model.dto.CooperativeStats;
import com.coopagri.service.CooperativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cooperatives")
@RequiredArgsConstructor
@Tag(name = "Coopératives", description = "API de gestion des coopératives agricoles")
@CrossOrigin(origins = "*")
public class CooperativeController {
    
    private final CooperativeService cooperativeService;
    
    @GetMapping
    @Operation(summary = "Liste toutes les coopératives")
    public ResponseEntity<List<Cooperative>> getAllCooperatives() {
        return ResponseEntity.ok(cooperativeService.getAllCooperatives());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Récupère une coopérative par son ID")
    public ResponseEntity<Cooperative> getCooperativeById(@PathVariable Long id) {
        return ResponseEntity.ok(cooperativeService.getCooperativeById(id));
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Récupère une coopérative par son type")
    public ResponseEntity<Cooperative> getCooperativeByType(@PathVariable String type) {
        return ResponseEntity.ok(cooperativeService.getCooperativeByType(type));
    }
    
    @GetMapping("/{type}/stats")
    @Operation(summary = "Récupère les statistiques d'une coopérative")
    public ResponseEntity<CooperativeStats> getCooperativeStats(@PathVariable String type) {
        return ResponseEntity.ok(cooperativeService.getCooperativeStats(type));
    }
    
    @GetMapping("/dashboard")
    @Operation(summary = "Données du tableau de bord")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        return ResponseEntity.ok(cooperativeService.getDashboardData());
    }
    
    @PostMapping
    @Operation(summary = "Crée une nouvelle coopérative")
    public ResponseEntity<Cooperative> createCooperative(@Valid @RequestBody Cooperative cooperative) {
        return ResponseEntity.ok(cooperativeService.createCooperative(cooperative));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Met à jour une coopérative")
    public ResponseEntity<Cooperative> updateCooperative(
            @PathVariable Long id,
            @Valid @RequestBody Cooperative cooperative) {
        return ResponseEntity.ok(cooperativeService.updateCooperative(id, cooperative));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprime une coopérative")
    public ResponseEntity<Void> deleteCooperative(@PathVariable Long id) {
        cooperativeService.deleteCooperative(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{coopId}/revenue-by-product")
    @Operation(summary = "Répartition des revenus par produit")
    public ResponseEntity<Map<String, Double>> getRevenueByProduct(@PathVariable Long coopId) {
        return ResponseEntity.ok(cooperativeService.getRevenueByProduct(coopId));
    }
}
