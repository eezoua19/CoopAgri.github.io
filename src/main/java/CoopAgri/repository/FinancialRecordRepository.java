package com.votreprojet.repository;

import com.votreprojet.model.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    
    List<FinancialRecord> findByRecordType(String recordType);
    List<FinancialRecord> findByRecordDateBetween(LocalDate startDate, LocalDate endDate);
    List<FinancialRecord> findByMemberId(Long memberId);
    List<FinancialRecord> findByStatus(String status);
    List<FinancialRecord> findByCategory(String category);
    long countByRecordType(String recordType);
}
