package com.coopagri.repository;

import com.coopagri.model.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CooperativeRepository extends JpaRepository<Cooperative, Long> {
    
    Optional<Cooperative> findByType(String type);
    
    List<Cooperative> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT c FROM Cooperative c WHERE c.totalMembers >= :minMembers")
    List<Cooperative> findByMinimumMembers(@Param("minMembers") Integer minMembers);
    
    @Query("SELECT c FROM Cooperative c ORDER BY c.totalRevenue DESC")
    List<Cooperative> findAllByRevenueDesc();
    
    @Query("SELECT SUM(c.totalRevenue) FROM Cooperative c")
    Double getTotalRevenue();
    
    @Query("SELECT c.type, COUNT(c) FROM Cooperative c GROUP BY c.type")
    List<Object[]> countByType();
}
