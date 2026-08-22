import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getReport, publishReport, acknowledgeReport } from "../api/reports";
import { useAuth } from "../auth/AuthContext";
import type { ShiftReport } from "../types";

function formatDate(iso: string): string {
    return new Date(iso).toLocaleString(undefined, {
        dateStyle: "medium",
        timeStyle: "short",
    });
}

export default function ReportDetailPage() {
    const { id } = useParams<{ id: string }>();
    const [report, setReport] = useState<ShiftReport | null>(null);
    const [error, setError] = useState("");
    const { refreshPendingCount } = useAuth();

    async function load() {
        if (!id) return;
        const data = await getReport(id);
        setReport(data);
    }

    useEffect(() => {
        load();
    }, [id]);

    async function handlePublish() {
        if (!id) return;
        setError("");
        try {
            await publishReport(id);
            await load();
        } catch (err: any) {
            setError(err.response?.data?.message ?? "Failed to publish");
        }
    }

    async function handleAcknowledge() {
        if (!id) return;
        setError("");
        try {
            await acknowledgeReport(id);
            await load();
            refreshPendingCount();
        } catch (err: any) {
            setError(err.response?.data?.message ?? "Failed to acknowledge");
        }
    }

    if (!report) return <p>Loading...</p>;

    return (
        <div>
            <div className="detail-header">
                <span className={`badge badge-${report.severity.toLowerCase()}`}>{report.severity}</span>
                <span className={`badge badge-${report.status.toLowerCase()}`}>{report.status}</span>
            </div>
            <p className="detail-meta">Author: {report.authorUsername}</p>
            <p className="detail-meta">Handoff to: {report.handoffToUsername ?? "—"}</p>
            <p className="detail-meta">Created: {formatDate(report.createdAt)}</p>
            {report.publishedAt && (
                <p className="detail-meta">Published: {formatDate(report.publishedAt)}</p>
            )}
            <p className="detail-meta">
                Acknowledged by: {report.acknowledgedByUsername ?? "—"}
                {report.acknowledgedAt && ` at ${formatDate(report.acknowledgedAt)}`}
            </p>

            <div className="detail-section">
                <h3>Active Incidents</h3>
                <p>{report.activeIncidents}</p>
            </div>
            <div className="detail-section">
                <h3>Ongoing Investigations</h3>
                <p>{report.ongoingInvestigations}</p>
            </div>
            <div className="detail-section">
                <h3>Watchlist Items</h3>
                <p>{report.watchlistItems}</p>
            </div>

            {report.status !== "DRAFT" && (
                <div className="detail-section">
                    <h3>System Snapshot</h3>
                    <p>{report.systemSnapshot || "No snapshot captured."}</p>
                </div>
            )}

            {error && <p className="error">{error}</p>}

            {report.status === "DRAFT" && (
                <div className="actions">
                    <button onClick={handlePublish}>Publish</button>
                </div>
            )}

            {report.status === "PUBLISHED" && (
                <div className="actions">
                    <button onClick={handleAcknowledge}>Acknowledge</button>
                </div>
            )}

            {report.history && report.history.length > 0 && (
                <div>
                    <h3>History</h3>
                    <ul className="history-list">
                        {report.history.map((h, i) => (
                            <li key={i}>
                                {h.fromStatus} → {h.toStatus} by {h.actorUsername} at {formatDate(h.occurredAt)}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}