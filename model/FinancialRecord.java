package com.coopagri.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "financial_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String type; // REVENUE, EXPENSE, PROFIT_DISTRIBUTION, INVESTMENT
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private LocalDate transactionDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id")
    private Cooperative cooperative;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    @Column(name = "payment_method")
    private String paymentMethod; // CASH, BANK_TRANSFER, MOBILE_MONEY
    
    @Column(name = "reference_number")
    private String referenceNumber;
    
    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = true;
}
