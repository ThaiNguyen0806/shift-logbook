package com.thai.shiftlogbook.dto;

import com.thai.shiftlogbook.domain.AuditLogEntry;

import java.time.Instant;

public class AuditLogEntryResponse {

    private final String fromStatus;
    private final String toStatus;
    private final String actorUsername;
    private final Instant occurredAt;

    public AuditLogEntryResponse(AuditLogEntry entry) {
        this.fromStatus = entry.getFromStatus().name();
        this.toStatus = entry.getToStatus().name();
        this.actorUsername = entry.getActor().getUsername();
        this.occurredAt = entry.getOccurredAt();
    }

    public String getFromStatus() { return fromStatus; }
    public String getToStatus() { return toStatus; }
    public String getActorUsername() { return actorUsername; }
    public Instant getOccurredAt() { return occurredAt; }
}