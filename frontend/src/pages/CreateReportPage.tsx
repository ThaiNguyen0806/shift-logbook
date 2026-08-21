import { useState, useEffect } from "react";
import type { FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { createReport } from "../api/reports";
import { getUsers } from "../api/users";
import type { Severity, UserSummary } from "../types";

export default function CreateReportPage() {
    const [activeIncidents, setActiveIncidents] = useState("");
    const [ongoingInvestigations, setOngoingInvestigations] = useState("");
    const [watchlistItems, setWatchlistItems] = useState("");
    const [severity, setSeverity] = useState<Severity>("LOW");
    const [tags, setTags] = useState("");
    const [handoffToUserId, setHandoffToUserId] = useState("");
    const [users, setUsers] = useState<UserSummary[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        getUsers().then(setUsers);
    }, []);

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        const report = await createReport({
            activeIncidents,
            ongoingInvestigations,
            watchlistItems,
            severity,
            tags,
            handoffToUserId,
        });
        navigate(`/reports/${report.id}`);
    }

    return (
        <div>
            <h1>New Report</h1>
            <p className="form-intro">
                Write this for whoever picks up after you - they weren't here, so include anything they'd need to know.
            </p>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Hand off to</label>
                    <select value={handoffToUserId} onChange={(e) => setHandoffToUserId(e.target.value)} required>
                        <option value="">Select a person</option>
                        {users.map((u) => (
                            <option key={u.id} value={u.id}>
                                {u.displayName} ({u.username})
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Active Incidents</label>
                    <p className="field-hint">Something broken right now.</p>
                    <textarea
                        value={activeIncidents}
                        onChange={(e) => setActiveIncidents(e.target.value)}
                        placeholder="e.g. Site is down"
                    />
                </div>
                <div>
                    <label>Ongoing Investigations</label>
                    <p className="field-hint">Still figuring out what's wrong.</p>
                    <textarea
                        value={ongoingInvestigations}
                        onChange={(e) => setOngoingInvestigations(e.target.value)}
                        placeholder="e.g. App is slow, not sure why yet"
                    />
                </div>
                <div>
                    <label>Watchlist Items</label>
                    <p className="field-hint">Not broken yet, but worth watching.</p>
                    <textarea
                        value={watchlistItems}
                        onChange={(e) => setWatchlistItems(e.target.value)}
                        placeholder="e.g. Storage is getting full"
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