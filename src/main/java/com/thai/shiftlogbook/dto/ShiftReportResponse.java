package com.thai.shiftlogbook.dto;

import com.thai.shiftlogbook.domain.ShiftReport;

import java.time.Instant;
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
    private final Instant createdAt;
    private final Instant publishedAt;
    private final Instant acknowledgedAt;

    public ShiftReportResponse(ShiftReport report) {
        this.id = report.getId();
        this.status = report.getStatus().name();
        this.activeIncidents = report.getActiveIncidents();
        this.ongoingInvestigations = report.getOngoingInvestigations();
        this.watchlistItems = report.getWatchlistItems();
        this.severity = report.getSeverity();
        this.tags = report.getTags();
        this.authorUsername = report.getAuthor().getUsername();
        this.acknowledgedByUsername = report.getAcknowledgedBy() != null
                ? report.getAcknowledgedBy().getUsername() : null;
        this.createdAt = report.getCreatedAt();
        this.publishedAt = report.getPublishedAt();
        this.acknowledgedAt = report.getAcknowledgedAt();
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
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPublishedAt() { return publishedAt; }
    public Instant getAcknowledgedAt() { return acknowledgedAt; }
}