package com.coopagri.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(name = "join_date")
    private LocalDate joinDate;
    
    @Column(name = "total_harvest")
    @Builder.Default
    private Double totalHarvest = 0.0;
    
    @Column(name = "total_earnings")
    @Builder.Default
    private Double totalEarnings = 0.0;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id")
    private Cooperative cooperative;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Harvest> harvests = new HashSet<>();
    
    // Méthodes utilitaires
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public void addHarvest(Harvest harvest) {
        harvest.setMember(this);
        this.harvests.add(harvest);
        this.totalHarvest += harvest.getQuantity();
        this.totalEarnings += harvest.getValue();
    }
}
