package com.coopagri.repository;

import com.coopagri.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByEmail(String email);
    
    List<Member> findByCooperativeId(Long cooperativeId);
    
    List<Member> findByIsActive(Boolean isActive);
    
    @Query("SELECT m FROM Member m WHERE m.cooperative.type = :coopType")
    List<Member> findByCooperativeType(@Param("coopType") String coopType);
    
    @Query("SELECT m FROM Member m WHERE m.joinDate BETWEEN :startDate AND :endDate")
    List<Member> findByJoinDateBetween(@Param("startDate") LocalDate startDate, 
                                      @Param("endDate") LocalDate endDate);
    
    @Query("SELECT m FROM Member m WHERE m.totalEarnings >= :minEarnings")
    List<Member> findByMinEarnings(@Param("minEarnings") Double minEarnings);
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.cooperative.id = :coopId AND m.isActive = true")
    Long countActiveMembersByCooperative(@Param("coopId") Long coopId);
}
