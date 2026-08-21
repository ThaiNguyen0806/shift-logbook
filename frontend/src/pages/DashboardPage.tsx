import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getReports, getMyDrafts, getPendingForMe } from "../api/reports";
import type { ShiftReport } from "../types";

type View = "all" | "drafts" | "pending";

export default function DashboardPage() {
    const [searchParams, setSearchParams] = useSearchParams();
    const view = (searchParams.get("view") as View) ?? "all";

    const [reports, setReports] = useState<ShiftReport[]>([]);
    const [severity, setSeverity] = useState("");

    async function load() {
        if (view === "drafts") {
            setReports(await getMyDrafts());
        } else if (view === "pending") {
            setReports(await getPendingForMe());
        } else {
            setReports(await getReports(severity ? { severity } : undefined));
        }
    }

    useEffect(() => {
        load();
    }, [view, severity]);

    function setView(next: View) {
        if (next === "all") {
            setSearchParams({});
        } else {
            setSearchParams({ view: next });
        }
    }

    const titles: Record<View, string> = {
        all: "Reports",
        drafts: "My Drafts",
        pending: "Pending My Acknowledgment",
    };

    const emptyMessages: Record<View, string> = {
        all: "No reports found.",
        drafts: "No drafts yet.",
        pending: "Nothing waiting on you right now.",
    };

    return (
        <div>
            <div className="toolbar">
                <h1>{titles[view]}</h1>
                <div className="filter-group">
                    {view === "all" && (
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
                    <button
                        className={view === "pending" ? "toggle-btn active" : "toggle-btn"}
                        onClick={() => setView(view === "pending" ? "all" : "pending")}
                    >
                        Pending for Me
                    </button>
                    <button
                        className={view === "drafts" ? "toggle-btn active" : "toggle-btn"}
                        onClick={() => setView(view === "drafts" ? "all" : "drafts")}
                    >
                        My Drafts
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

            {reports.length === 0 && <p className="empty-state">{emptyMessages[view]}</p>}
        </div>
    );
}