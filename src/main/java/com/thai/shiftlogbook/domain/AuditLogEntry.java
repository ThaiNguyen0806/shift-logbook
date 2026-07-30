package com.thai.shiftlogbook.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
public class AuditLogEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID reportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus toStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    protected AuditLogEntry() {
    }

    public AuditLogEntry(UUID reportId, ReportStatus fromStatus, ReportStatus toStatus, User actor) {
        this.reportId = reportId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.actor = actor;
    }

    @PrePersist
    void onCreate() {
        if (occurredAt == null) {
            occurredAt = Instant.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getReportId() {
        return reportId;
    }

    public ReportStatus getFromStatus() {
        return fromStatus;
    }

    public ReportStatus getToStatus() {
        return toStatus;
    }

    public User getActor() {
        return actor;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}