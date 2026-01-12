package com.coopagri.services;

import com.coopagri.models.Cooperative;
import com.coopagri.models.Harvest;
import com.coopagri.repository.CooperativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CooperativeService {
    
    @Autowired
    private CooperativeRepository repository;
    
    public List<Cooperative> findAll() {
        return repository.findAll();
    }
    
    public Cooperative findByType(String type) {
        return repository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));
    }
    
    // Calculer les bénéfices avec logique métier Java
    public double calculateProfits(String cooperativeType, int month, int year) {
        Cooperative coop = findByType(cooperativeType);
        List<Harvest> harvests = coop.getHarvestsByMonth(month, year);
        
        double totalRevenue = harvests.stream()
                .mapToDouble(h -> h.getQuantity() * h.getPricePerUnit())
                .sum();
        
        double totalCosts = harvests.stream()
                .mapToDouble(Harvest::getProductionCost)
                .sum();
        
        return totalRevenue - totalCosts;
    }
    
    // Générer des statistiques avancées
    public Map<String, Object> getStatistics(String cooperativeType) {
        Cooperative coop = findByType(cooperativeType);
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalMembers", coop.getActiveMembers().size());
        stats.put("monthlyHarvest", coop.getMonthlyHarvest(LocalDate.now()));
        stats.put("projectedRevenue", coop.calculateProjectedRevenue());
        stats.put("profitMargin", coop.calculateProfitMargin());
        
        return stats;
    }
}