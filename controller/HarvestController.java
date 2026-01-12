package com.coopagri.controller;

import com.coopagri.model.Harvest;
import com.coopagri.model.dto.HarvestRequest;
import com.coopagri.service.HarvestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/harvests")
@RequiredArgsConstructor
@Tag(name = "Récoltes", description = "API de gestion des récoltes")
@CrossOrigin(origins = "*")
public class HarvestController {
    
    private final HarvestService harvestService;
    
    @GetMapping
    @Operation(summary = "Liste toutes les récoltes")
    public ResponseEntity<List<Harvest>> getAllHarvests() {
        return ResponseEntity.ok(harvestService.getAllHarvests());
    }
    
    @GetMapping("/cooperative/{cooperativeId}")
    @Operation(summary = "Liste les récoltes d'une coopérative")
    public ResponseEntity<List<Harvest>> getHarvestsByCooperative(@PathVariable Long cooperativeId) {
        return ResponseEntity.ok(harvestService.getHarvestsByCooperative(cooperativeId));
    }
    
    @GetMapping("/member/{memberId}")
    @Operation(summary = "Liste les récoltes d'un membre")
    public ResponseEntity<List<Harvest>> getHarvestsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(harvestService.getHarvestsByMember(memberId));
    }
    
    @PostMapping
    @Operation(summary = "Enregistre une nouvelle récolte")
    public ResponseEntity<Harvest> createHarvest(@Valid @RequestBody HarvestRequest request) {
        return ResponseEntity.ok(harvestService.createHarvest(request));
    }
    
    @GetMapping("/trend/{cooperativeId}")
    @Operation(summary = "Tendance des récoltes sur les derniers mois")
    public ResponseEntity<Map<String, Double>> getHarvestTrend(
            @PathVariable Long cooperativeId,
            @RequestParam(defaultValue = "6") int months) {
        return ResponseEntity.ok(harvestService.getMonthlyHarvestTrend(cooperativeId, months));
    }
}
