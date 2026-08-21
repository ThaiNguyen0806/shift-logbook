package com.thai.shiftlogbook.dto;

import com.thai.shiftlogbook.domain.Severity;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateReportRequest {

    private String activeIncidents;
    private String ongoingInvestigations;
    private String watchlistItems;

    @NotNull
    private Severity severity;

    private String tags;

    @NotNull
    private UUID handoffToUserId;

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

    public UUID getHandoffToUserId() { return handoffToUserId; }
    public void setHandoffToUserId(UUID handoffToUserId) { this.handoffToUserId = handoffToUserId; }
}