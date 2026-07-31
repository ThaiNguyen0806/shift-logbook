package com.thai.shiftlogbook.repository;

import com.thai.shiftlogbook.domain.AuditLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLogEntry, UUID> {
    List<AuditLogEntry> findByReportIdOrderByOccurredAtAsc(UUID reportId);
}