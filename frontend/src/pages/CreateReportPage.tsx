import { useState, FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { createReport } from "../api/reports";
import type { Severity } from "../types";

export default function CreateReportPage() {
    const [activeIncidents, setActiveIncidents] = useState("");
    const [ongoingInvestigations, setOngoingInvestigations] = useState("");
    const [watchlistItems, setWatchlistItems] = useState("");
    const [severity, setSeverity] = useState<Severity>("LOW");
    const [tags, setTags] = useState("");
    const navigate = useNavigate();

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        const report = await createReport({
            activeIncidents,
            ongoingInvestigations,
            watchlistItems,
            severity,
            tags,
        });
        navigate(`/reports/${report.id}`);
    }

    return (
        <div>
            <h1>New Report</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Active Incidents</label>
                    <p className="field-hint">Anything currently ongoing that the next shift needs to know about.</p>
                    <textarea value={activeIncidents} onChange={(e) => setActiveIncidents(e.target.value)} placeholder="e.g. Checkout page is down since 2 PM" />
                </div>
                <div>
                    <label>Ongoing Investigations</label>
                    <p className="field-hint">Issues you're still digging into - not resolved, but not a live incident either.</p>
                    <textarea value={ongoingInvestigations} onChange={(e) => setOngoingInvestigations(e.target.value)} placeholder="e.g. Database is running slow, not sure why yet"
                    />
                </div>
                <div>
                    <label>Watchlist Items</label>
                    <p className="field-hint">Things that aren't a problem yet, but worth keeping an eye on.</p>
                    <textarea value={watchlistItems} onChange={(e) => setWatchlistItems(e.target.value)} placeholder="e.g. Memory usage on server climbing slowly, worth keeping an eye on"
                    />
                </div>
                <div>
                    <label>Severity</label>
                    <select value={severity} onChange={(e) => setSeverity(e.target.value as Severity)}>
                        <option value="LOW">Low</option>
                        <option value="MEDIUM">Medium</option>
                        <option value="HIGH">High</option>
                        <option value="CRITICAL">Critical</option>
                    </select>
                </div>
                <div>
                    <label>Tags (comma-separated)</label>
                    <input value={tags} onChange={(e) => setTags(e.target.value)} />
                </div>
                <button type="submit">Save Draft</button>
            </form>
        </div>
    );
}