package com.thai.shiftlogbook.repository;

import com.thai.shiftlogbook.domain.ShiftReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ShiftReportRepository extends JpaRepository<ShiftReport, UUID> {

    @Query(value = """
        SELECT * FROM shift_reports
        WHERE (:severity IS NULL OR severity = :severity)
          AND (:tag IS NULL OR tags LIKE CONCAT('%', :tag, '%'))
          AND (CAST(:since AS timestamptz) IS NULL OR created_at >= CAST(:since AS timestamptz))
        ORDER BY created_at DESC
        """, nativeQuery = true)
    List<ShiftReport> search(@Param("severity") String severity,
                             @Param("tag") String tag,
                             @Param("since") Instant since);
}