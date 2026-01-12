package com.coopagri.service;

import com.coopagri.model.Cooperative;
import com.coopagri.model.dto.CooperativeStats;
import com.coopagri.repository.CooperativeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CooperativeService {
    
    private final CooperativeRepository cooperativeRepository;
    private final HarvestService harvestService;
    private final FinancialService financialService;
    
    public List<Cooperative> getAllCooperatives() {
        log.info("Fetching all cooperatives");
        return cooperativeRepository.findAll();
    }
    
    public Cooperative getCooperativeById(Long id) {
        log.info("Fetching cooperative with id: {}", id);
        return cooperativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coopérative non trouvée avec id: " + id));
    }
    
    public Cooperative getCooperativeByType(String type) {
        log.info("Fetching cooperative with type: {}", type);
        return cooperativeRepository.findByType(type)
                .orElseThrow(() -> new RuntimeException("Coopérative non trouvée avec type: " + type));
    }
    
    @Transactional
    public Cooperative createCooperative(Cooperative cooperative) {
        log.info("Creating new cooperative: {}", cooperative.getName());
        return cooperativeRepository.save(cooperative);
    }
    
    @Transactional
    public Cooperative updateCooperative(Long id, Cooperative cooperativeDetails) {
        log.info("Updating cooperative with id: {}", id);
        Cooperative cooperative = getCooperativeById(id);
        
        cooperative.setName(cooperativeDetails.getName());
        cooperative.setDescription(cooperativeDetails.getDescription());
        cooperative.setTotalMembers(cooperativeDetails.getTotalMembers());
        cooperative.setTotalHarvest(cooperativeDetails.getTotalHarvest());
        cooperative.setTotalRevenue(cooperativeDetails.getTotalRevenue());
        
        return cooperativeRepository.save(cooperative);
    }
    
    @Transactional
    public void deleteCooperative(Long id) {
        log.info("Deleting cooperative with id: {}", id);
        cooperativeRepository.deleteById(id);
    }
    
    @Cacheable(value = "cooperativeStats", key = "#type")
    public CooperativeStats getCooperativeStats(String type) {
        log.info("Calculating stats for cooperative: {}", type);
        Cooperative cooperative = getCooperativeByType(type);
        
        double monthlyRevenue = financialService.getMonthlyRevenue(cooperative.getId(), 
            YearMonth.now().getMonthValue(), YearMonth.now().getYear());
        
        double monthlyProfit = financialService.getMonthlyProfit(cooperative.getId(), 
            YearMonth.now().getMonthValue(), YearMonth.now().getYear());
        
        int newMembersThisMonth = harvestService.countNewMembersThisMonth(cooperative.getId());
        double harvestThisMonth = harvestService.getHarvestThisMonth(cooperative.getId());
        
        return CooperativeStats.builder()
                .cooperativeName(cooperative.getName())
                .totalMembers(cooperative.getTotalMembers())
                .totalHarvest(cooperative.getTotalHarvest())
                .totalRevenue(cooperative.getTotalRevenue())
                .monthlyRevenue(monthlyRevenue)
                .monthlyProfit(monthlyProfit)
                .newMembersThisMonth(newMembersThisMonth)
                .harvestThisMonth(harvestThisMonth)
                .growthRate(calculateGrowthRate(cooperative))
                .build();
    }
    
    public Map<String, Double> getRevenueByProduct(Long cooperativeId) {
        log.info("Getting revenue by product for cooperative: {}", cooperativeId);
        Map<String, Double> revenueByProduct = new HashMap<>();
        
        // Cacao
        Double cacaoRevenue = harvestService.getRevenueByProduct(cooperativeId, "cacao");
        revenueByProduct.put("cacao", cacaoRevenue != null ? cacaoRevenue : 0.0);
        
        // Anacarde
        Double anacardeRevenue = harvestService.getRevenueByProduct(cooperativeId, "anacarde");
        revenueByProduct.put("anacarde", anacardeRevenue != null ? anacardeRevenue : 0.0);
        
        // Huile de palme
        Double palmOilRevenue = harvestService.getRevenueByProduct(cooperativeId, "huile_palme");
        revenueByProduct.put("palmier", palmOilRevenue != null ? palmOilRevenue : 0.0);
        
        return revenueByProduct;
    }
    
    public Map<String, Object> getDashboardData() {
        log.info("Generating dashboard data");
        Map<String, Object> dashboard = new HashMap<>();
        
        // Totaux globaux
        Double totalRevenue = cooperativeRepository.getTotalRevenue();
        Long totalMembers = cooperativeRepository.findAll().stream()
                .mapToLong(c -> c.getTotalMembers())
                .sum();
        
        // Par coopérative
        List<Cooperative> cooperatives = cooperativeRepository.findAll();
        Map<String, Map<String, Object>> coopData = new HashMap<>();
        
        for (Cooperative coop : cooperatives) {
            Map<String, Object> data = new HashMap<>();
            data.put("name", coop.getName());
            data.put("members", coop.getTotalMembers());
            data.put("revenue", coop.getTotalRevenue());
            data.put("harvest", coop.getTotalHarvest());
            data.put("profit", coop.getMonthlyProfit());
            
            coopData.put(coop.getType(), data);
        }
        
        dashboard.put("totalRevenue", totalRevenue != null ? totalRevenue : 0);
        dashboard.put("totalMembers", totalMembers);
        dashboard.put("cooperatives", coopData);
        dashboard.put("lastUpdate", LocalDate.now());
        
        return dashboard;
    }
    
    private double calculateGrowthRate(Cooperative cooperative) {
        // Logique de calcul du taux de croissance
        double previousMonthRevenue = financialService.getPreviousMonthRevenue(cooperative.getId());
        double currentMonthRevenue = cooperative.getTotalRevenue();
        
        if (previousMonthRevenue == 0) return 0.0;
        
        return ((currentMonthRevenue - previousMonthRevenue) / previousMonthRevenue) * 100;
    }
}
