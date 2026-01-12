package com.coopagri.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cooperatives")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cooperative {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String type; // cacao, anacarde, palmier
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Column(name = "created_date")
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "total_members")
    @Builder.Default
    private Integer totalMembers = 0;
    
    @Column(name = "total_harvest")
    @Builder.Default
    private Double totalHarvest = 0.0;
    
    @Column(name = "total_revenue")
    @Builder.Default
    private Double totalRevenue = 0.0;
    
    @Column(name = "monthly_profit")
    @Builder.Default
    private Double monthlyProfit = 0.0;
    
    @Column(name = "growth_rate")
    @Builder.Default
    private Double growthRate = 0.0;
    
    @OneToMany(mappedBy = "cooperative", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Member> members = new HashSet<>();
    
    @OneToMany(mappedBy = "cooperative", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Harvest> harvests = new HashSet<>();
    
    // Méthodes utilitaires
    public void addHarvest(Harvest harvest) {
        harvest.setCooperative(this);
        this.harvests.add(harvest);
        this.totalHarvest += harvest.getQuantity();
        calculateProfit();
    }
    
    public void addMember(Member member) {
        member.setCooperative(this);
        this.members.add(member);
        this.totalMembers = this.members.size();
    }
    
    private void calculateProfit() {
        this.monthlyProfit = this.totalRevenue * 0.15; // 15% de marge
    }
}
