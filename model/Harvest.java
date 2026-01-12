package com.coopagri.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "harvests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Harvest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String product; // cacao, anacarde, huile_palme
    
    @Column(nullable = false)
    private Double quantity; // en kg ou litres
    
    @Column(nullable = false)
    private String unit; // kg, L
    
    @Column(nullable = false)
    private LocalDate harvestDate;
    
    @Column(nullable = false)
    private String quality; // EXCELLENT, GOOD, AVERAGE, POOR
    
    @Column(name = "unit_price")
    private Double unitPrice = 1000.0; // Prix unitaire en FCFA
    
    @Column(name = "total_value")
    private Double totalValue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Cooperative cooperative;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @PrePersist
    @PreUpdate
    public void calculateValue() {
        if (this.unitPrice == null) {
            // Prix par défaut selon le produit
            switch (this.product.toLowerCase()) {
                case "cacao": this.unitPrice = 1500.0; break;
                case "anacarde": this.unitPrice = 1200.0; break;
                case "huile_palme": this.unitPrice = 800.0; break;
                default: this.unitPrice = 1000.0;
            }
        }
        this.totalValue = this.quantity * this.unitPrice;
    }
}
