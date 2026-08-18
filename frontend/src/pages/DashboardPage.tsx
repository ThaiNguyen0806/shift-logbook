import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getReports, getMyDrafts } from "../api/reports";
import type { ShiftReport } from "../types";

export default function DashboardPage() {
    const [reports, setReports] = useState<ShiftReport[]>([]);
    const [severity, setSeverity] = useState("");
    const [showingDrafts, setShowingDrafts] = useState(false);

    async function load() {
        const data = showingDrafts
            ? await getMyDrafts()
            : await getReports(severity ? { severity } : undefined);
        setReports(data);
    }

    useEffect(() => {
        load();
    }, [severity, showingDrafts]);

    return (
        <div>
            <div className="toolbar">
                <h1>{showingDrafts ? "My Drafts" : "Reports"}</h1>
                <div className="filter-group">
                    {!showingDrafts && (
                        <>
                            <label>Severity</label>
                            <select value={severity} onChange={(e) => setSeverity(e.target.value)}>
                                <option value="">All</option>
                                <option value="LOW">Low</option>
                                <option value="MEDIUM">Medium</option>
                                <option value="HIGH">High</option>
                                <option value="CRITICAL">Critical</option>
                            </select>
                        </>
                    )}
                    <button className="toggle-btn" onClick={() => setShowingDrafts(!showingDrafts)}>
                        {showingDrafts ? "View All Reports" : "My Drafts"}
                    </button>
                </div>
            </div>

            <ul>
                {reports.map((r) => (
                    <li key={r.id}>
                        <Link to={`/reports/${r.id}`} className="report-card">
                            <span className={`badge badge-${r.severity.toLowerCase()}`}>{r.severity}</span>
                            <span className={`badge badge-${r.status.toLowerCase()}`}>{r.status}</span>
                            <span className="report-author">by {r.authorUsername}</span>
                        </Link>
                    </li>
                ))}
            </ul>

            {reports.length === 0 && (
                <p className="empty-state">
                    {showingDrafts ? "No drafts yet." : "No reports found."}
                </p>
            )}
        </div>
    );
}