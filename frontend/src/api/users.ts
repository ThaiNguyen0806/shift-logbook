import client from "./client";
import type { UserSummary } from "../types";

export async function getUsers(): Promise<UserSummary[]> {
    const res = await client.get<UserSummary[]>("/users");
    return res.data;
}