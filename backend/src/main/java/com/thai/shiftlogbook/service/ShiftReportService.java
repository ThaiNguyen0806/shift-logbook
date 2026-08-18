package com.thai.shiftlogbook.service;

import com.thai.shiftlogbook.domain.AuditLogEntry;
import com.thai.shiftlogbook.domain.ReportStatus;
import com.thai.shiftlogbook.domain.ShiftReport;
import com.thai.shiftlogbook.domain.User;
import com.thai.shiftlogbook.exception.IllegalTransitionException;
import com.thai.shiftlogbook.exception.ReportNotEditableException;
import com.thai.shiftlogbook.repository.AuditLogRepository;
import com.thai.shiftlogbook.repository.ShiftReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.thai.shiftlogbook.domain.Severity;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ShiftReportService {

    private final ShiftReportRepository reportRepository;
    private final AuditLogRepository auditLogRepository;

    public ShiftReportService(ShiftReportRepository reportRepository, AuditLogRepository auditLogRepository) {
        this.reportRepository = reportRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public ShiftReport createDraft(User author, String activeIncidents, String ongoingInvestigations,
                                   String watchlistItems, Severity severity, String tags) {
        ShiftReport report = new ShiftReport(author, activeIncidents, ongoingInvestigations,
                watchlistItems, severity, tags);
        return reportRepository.save(report);
    }

    @Transactional
    public ShiftReport updateDraft(UUID reportId, String activeIncidents, String ongoingInvestigations,
                                   String watchlistItems, Severity severity, String tags) {
        ShiftReport report = getOrThrow(reportId);

        if (!report.getStatus().isEditable()) {
            throw new ReportNotEditableException(report.getStatus());
        }

        report.setActiveIncidents(activeIncidents);
        report.setOngoingInvestigations(ongoingInvestigations);
        report.setWatchlistItems(watchlistItems);
        report.setSeverity(severity);
        report.setTags(tags);

        return reportRepository.save(report);
    }

    @Transactional
    public ShiftReport publish(UUID reportId, User actor, String systemSnapshot) {
        ShiftReport report = getOrThrow(reportId);

        if (!report.getAuthor().getId().equals(actor.getId())) {
            throw new IllegalStateException("Only the author can publish this report");
        }

        ShiftReport transitioned = transition(reportId, ReportStatus.PUBLISHED, actor);
        transitioned.setPublishedAt(Instant.now());
        transitioned.setSystemSnapshot(systemSnapshot);
        return reportRepository.save(transitioned);
    }

    @Transactional
    public ShiftReport acknowledge(UUID reportId, User actor) {
        ShiftReport report = getOrThrow(reportId);

        if (report.getAuthor().getId().equals(actor.getId())) {
            throw new IllegalStateException("Cannot acknowledge your own report");
        }

        ShiftReport transitioned = transition(reportId, ReportStatus.ACKNOWLEDGED, actor);
        transitioned.setAcknowledgedAt(Instant.now());
        transitioned.setAcknowledgedBy(actor);
        return reportRepository.save(transitioned);
    }

    private ShiftReport transition(UUID reportId, ReportStatus target, User actor) {
        ShiftReport report = getOrThrow(reportId);
        ReportStatus current = report.getStatus();

        if (!current.canTransitionTo(target)) {
            throw new IllegalTransitionException(current, target);
        }

        report.setStatus(target);

        AuditLogEntry entry = new AuditLogEntry(reportId, current, target, actor);
        auditLogRepository.save(entry);

        return report;
    }

    private ShiftReport getOrThrow(UUID reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new NoSuchElementException("No report with id " + reportId));
    }

    @Transactional(readOnly = true)
    public ShiftReport getOrThrowPublic(UUID reportId) {
        return getOrThrow(reportId);
    }

    @Transactional(readOnly = true)
    public List<ShiftReport> getAll() {
        return reportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ShiftReport> search(String severity, String tag, Instant since) {
        return reportRepository.search(severity, tag, since);
    }

    @Transactional(readOnly = true)
    public List<ShiftReport> getMyDrafts(User user) {
        return reportRepository.findMyDrafts(user.getId());
    }
}