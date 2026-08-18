import client from "./client";
import type { ShiftReport, CreateReportPayload } from "../types";

export async function getReports(filters?: {
    severity?: string;
    tag?: string;
    since?: string;
}): Promise<ShiftReport[]> {
    const res = await client.get<ShiftReport[]>("/reports", { params: filters });
    return res.data;
}

export async function getReport(id: string): Promise<ShiftReport> {
    const res = await client.get<ShiftReport>(`/reports/${id}`);
    return res.data;
}

export async function createReport(payload: CreateReportPayload): Promise<ShiftReport> {
    const res = await client.post<ShiftReport>("/reports", payload);
    return res.data;
}

export async function updateReport(
    id: string,
    payload: CreateReportPayload
): Promise<ShiftReport> {
    const res = await client.put<ShiftReport>(`/reports/${id}`, payload);
    return res.data;
}

export async function publishReport(id: string, systemSnapshot: string): Promise<ShiftReport> {
    const res = await client.post<ShiftReport>(`/reports/${id}/publish`, { systemSnapshot });
    return res.data;
}

export async function acknowledgeReport(id: string): Promise<ShiftReport> {
    const res = await client.post<ShiftReport>(`/reports/${id}/acknowledge`);
    return res.data;
}

export async function getMyDrafts(): Promise<ShiftReport[]> {
    const res = await client.get<ShiftReport[]>("/reports/my-drafts");
    return res.data;
}