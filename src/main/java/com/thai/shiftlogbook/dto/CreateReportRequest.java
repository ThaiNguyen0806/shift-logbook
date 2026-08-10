package com.thai.shiftlogbook.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateReportRequest {

    private String activeIncidents;
    private String ongoingInvestigations;
    private String watchlistItems;

    @NotBlank
    private String severity;

    private String tags;

    public String getActiveIncidents() { return activeIncidents; }
    public void setActiveIncidents(String activeIncidents) { this.activeIncidents = activeIncidents; }

    public String getOngoingInvestigations() { return ongoingInvestigations; }
    public void setOngoingInvestigations(String ongoingInvestigations) { this.ongoingInvestigations = ongoingInvestigations; }

    public String getWatchlistItems() { return watchlistItems; }
    public void setWatchlistItems(String watchlistItems) { this.watchlistItems = watchlistItems; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}