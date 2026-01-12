package com.coopagri.service;

import com.coopagri.model.Cooperative;
import com.coopagri.model.Harvest;
import com.coopagri.model.Member;
import com.coopagri.model.dto.HarvestRequest;
import com.coopagri.repository.CooperativeRepository;
import com.coopagri.repository.HarvestRepository;
import com.coopagri.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HarvestService {
    
    private final HarvestRepository harvestRepository;
    private final CooperativeRepository cooperativeRepository;
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    
    public List<Harvest> getAllHarvests() {
        log.info("Fetching all harvests");
        return harvestRepository.findAll();
    }
    
    public List<Harvest> getHarvestsByCooperative(Long cooperativeId) {
        log.info("Fetching harvests for cooperative: {}", cooperativeId);
        return harvestRepository.findByCooperativeId(cooperativeId);
    }
    
    public List<Harvest> getHarvestsByMember(Long memberId) {
        log.info("Fetching harvests for member: {}", memberId);
        return harvestRepository.findByMemberId(memberId);
    }
    
    @Transactional
    public Harvest createHarvest(HarvestRequest request) {
        log.info("Creating new harvest: {}", request);
        
        Cooperative cooperative = cooperativeRepository.findById(request.getCooperativeId())
                .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));
        
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Membre non trouvé"));
        
        Harvest harvest = Harvest.builder()
                .product(request.getProduct())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .harvestDate(request.getHarvestDate())
                .quality(request.getQuality())
                .unitPrice(request.getUnitPrice())
                .cooperative(cooperative)
                .member(member)
                .build();
        
        // Mettre à jour les totaux
        cooperative.addHarvest(harvest);
        member.addHarvest(harvest);
        
        // Sauvegarder
        Harvest savedHarvest = harvestRepository.save(harvest);
        
        // Notification
        sendHarvestNotification(savedHarvest);
        
        return savedHarvest;
    }
    
    public Double getHarvestThisMonth(Long cooperativeId) {
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();
        
        return harvestRepository.getTotalHarvestByCooperativeAndDate(cooperativeId, start, end);
    }
    
    public Double getRevenueByProduct(Long cooperativeId, String product) {
        return harvestRepository.getTotalRevenueByProduct(cooperativeId, product);
    }
    
    public Map<String, Double> getMonthlyHarvestTrend(Long cooperativeId, int months) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);
        
        List<Object[]> results = harvestRepository.getHarvestTrend(cooperativeId, startDate, endDate);
        
        return results.stream()
                .collect(Collectors.toMap(
                    row -> row[0].toString(), // Mois
                    row -> (Double) row[1]   // Quantité
                ));
    }
    
    public Integer countNewMembersThisMonth(Long cooperativeId) {
        LocalDate now = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(now);
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();
        
        return memberRepository.findByJoinDateBetween(start, end).size();
    }
    
    private void sendHarvestNotification(Harvest harvest) {
        try {
            String subject = "Nouvelle récolte enregistrée";
            String content = String.format("""
                Bonjour %s,
                
                Votre récolte a été enregistrée avec succès :
                - Produit: %s
                - Quantité: %s %s
                - Qualité: %s
                - Valeur estimée: %,.2f FCFA
                
                Cordialement,
                L'équipe CoopAgri
                """,
                harvest.getMember().getFirstName(),
                harvest.getProduct(),
                harvest.getQuantity(),
                harvest.getUnit(),
                harvest.getQuality(),
                harvest.getTotalValue()
            );
            
            emailService.sendEmail(
                harvest.getMember().getEmail(),
                subject,
                content
            );
            
            log.info("Notification envoyée à {}", harvest.getMember().getEmail());
            
        } catch (Exception e) {
            log.error("Erreur d'envoi de notification: {}", e.getMessage());
        }
    }
}
