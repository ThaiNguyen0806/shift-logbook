package com.thai.shiftlogbook.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shift_reports")
public class ShiftReport {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;

    @Column(columnDefinition = "TEXT")
    private String activeIncidents;

    @Column(columnDefinition = "TEXT")
    private String ongoingInvestigations;

    @Column(columnDefinition = "TEXT")
    private String watchlistItems;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    private String tags;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "acknowledged_by_id")
    private User acknowledgedBy;

    @ManyToOne
    @JoinColumn(name = "handoff_to_id")
    private User handoffToUser;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant publishedAt;

    private Instant acknowledgedAt;

    @Column(columnDefinition = "TEXT")
    private String systemSnapshot;

    protected ShiftReport() {
    }

    public ShiftReport(User author, String activeIncidents, String ongoingInvestigations,
                       String watchlistItems, Severity severity, String tags, User handoffToUser) {
        this.author = author;
        this.activeIncidents = activeIncidents;
        this.ongoingInvestigations = ongoingInvestigations;
        this.watchlistItems = watchlistItems;
        this.severity = severity;
        this.tags = tags;
        this.handoffToUser = handoffToUser;
        this.status = ReportStatus.DRAFT;
    }

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public UUID getId() { return id; }

    public ReportStatus getStatus() { return status; }
    public void setStatus(ReportStatus status) { this.status = status; }

    public String getActiveIncidents() { return activeIncidents; }
    public void setActiveIncidents(String activeIncidents) { this.activeIncidents = activeIncidents; }

    public String getOngoingInvestigations() { return ongoingInvestigations; }
    public void setOngoingInvestigations(String ongoingInvestigations) { this.ongoingInvestigations = ongoingInvestigations; }

    public String getWatchlistItems() { return watchlistItems; }
    public void setWatchlistItems(String watchlistItems) { this.watchlistItems = watchlistItems; }

    public Severity getSeverity() { return severity; }
    public void setSeverity(Severity severity) { this.severity = severity; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public User getAuthor() { return author; }

    public User getAcknowledgedBy() { return acknowledgedBy; }
    public void setAcknowledgedBy(User acknowledgedBy) { this.acknowledgedBy = acknowledgedBy; }

    public User getHandoffToUser() { return handoffToUser; }

    public Instant getCreatedAt() { return createdAt; }

    public Instant getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }

    public Instant getAcknowledgedAt() { return acknowledgedAt; }
    public void setAcknowledgedAt(Instant acknowledgedAt) { this.acknowledgedAt = acknowledgedAt; }

    public String getSystemSnapshot() { return systemSnapshot; }
    public void setSystemSnapshot(String systemSnapshot) { this.systemSnapshot = systemSnapshot; }
}