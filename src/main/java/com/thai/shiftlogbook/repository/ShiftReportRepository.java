package com.thai.shiftlogbook.repository;

import com.thai.shiftlogbook.domain.ShiftReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ShiftReportRepository
        extends JpaRepository<ShiftReport, UUID>, JpaSpecificationExecutor<ShiftReport> {
}
