export type ReportStatus = "DRAFT" | "PUBLISHED" | "ACKNOWLEDGED";
export type Severity = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";

export interface AuditLogEntry {
    fromStatus: ReportStatus;
    toStatus: ReportStatus;
    actorUsername: string;
    occurredAt: string;
}

export interface ShiftReport {
    id: string;
    status: ReportStatus;
    activeIncidents: string;
    ongoingInvestigations: string;
    watchlistItems: string;
    severity: Severity;
    tags: string;
    authorUsername: string;
    acknowledgedByUsername: string | null;
    handoffToUsername: string | null;
    createdAt: string;
    publishedAt: string | null;
    acknowledgedAt: string | null;
    history: AuditLogEntry[] | null;
    systemSnapshot: string | null;
}

export interface LoginResponse {
    token: string;
}

export interface CreateReportPayload {
    activeIncidents: string;
    ongoingInvestigations: string;
    watchlistItems: string;
    severity: Severity;
    tags: string;
    handoffToUserId: string;
}

export interface ErrorResponse {
    message: string;
    timestamp: string;
}

export interface UserSummary {
    id: string;
    username: string;
    displayName: string;
}