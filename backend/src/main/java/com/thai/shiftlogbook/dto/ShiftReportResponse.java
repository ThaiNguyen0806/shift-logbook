package com.thai.shiftlogbook.dto;

import com.thai.shiftlogbook.domain.ShiftReport;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ShiftReportResponse {

    private final UUID id;
    private final String status;
    private final String activeIncidents;
    private final String ongoingInvestigations;
    private final String watchlistItems;
    private final String severity;
    private final String tags;
    private final String authorUsername;
    private final String acknowledgedByUsername;
    private final String handoffToUsername;
    private final Instant createdAt;
    private final Instant publishedAt;
    private final Instant acknowledgedAt;
    private final String systemSnapshot;
    private final List<AuditLogEntryResponse> history;

    public ShiftReportResponse(ShiftReport report) {
        this(report, null);
    }

    public ShiftReportResponse(ShiftReport report, List<AuditLogEntryResponse> history) {
        this.id = report.getId();
        this.status = report.getStatus().name();
        this.activeIncidents = report.getActiveIncidents();
        this.ongoingInvestigations = report.getOngoingInvestigations();
        this.watchlistItems = report.getWatchlistItems();
        this.severity = report.getSeverity().name();
        this.tags = report.getTags();
        this.authorUsername = report.getAuthor().getUsername();
        this.acknowledgedByUsername = report.getAcknowledgedBy() != null
                ? report.getAcknowledgedBy().getUsername() : null;
        this.handoffToUsername = report.getHandoffToUser() != null
                ? report.getHandoffToUser().getUsername() : null;
        this.createdAt = report.getCreatedAt();
        this.publishedAt = report.getPublishedAt();
        this.acknowledgedAt = report.getAcknowledgedAt();
        this.systemSnapshot = report.getSystemSnapshot();
        this.history = history;
    }

    public UUID getId() { return id; }
    public String getStatus() { return status; }
    public String getActiveIncidents() { return activeIncidents; }
    public String getOngoingInvestigations() { return ongoingInvestigations; }
    public String getWatchlistItems() { return watchlistItems; }
    public String getSeverity() { return severity; }
    public String getTags() { return tags; }
    public String getAuthorUsername() { return authorUsername; }
    public String getAcknowledgedByUsername() { return acknowledgedByUsername; }
    public String getHandoffToUsername() { return handoffToUsername; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPublishedAt() { return publishedAt; }
    public Instant getAcknowledgedAt() { return acknowledgedAt; }
    public String getSystemSnapshot() { return systemSnapshot; }
    public List<AuditLogEntryResponse> getHistory() { return history; }
}