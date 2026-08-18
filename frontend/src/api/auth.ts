import client from "./client";
import type { LoginResponse } from "../types";

export async function login(username: string, password: string): Promise<LoginResponse> {
    const res = await client.post<LoginResponse>("/auth/login", { username, password });
    return res.data;
}

export async function register(
    username: string,
    password: string,
    displayName: string
): Promise<LoginResponse> {
    const res = await client.post<LoginResponse>("/auth/register", {
        username,
        password,
        displayName,
    });
    return res.data;
}