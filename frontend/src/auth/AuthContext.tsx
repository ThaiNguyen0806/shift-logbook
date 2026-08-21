import { createContext, useContext, useState, ReactNode, useEffect } from "react";
import { login as apiLogin, register as apiRegister } from "../api/auth";
import { getPendingForMe } from "../api/reports";

interface AuthContextType {
    token: string | null;
    isAuthenticated: boolean;
    pendingCount: number;
    refreshPendingCount: () => void;
    login: (username: string, password: string) => Promise<void>;
    register: (username: string, password: string, displayName: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [token, setToken] = useState<string | null>(localStorage.getItem("token"));
    const [pendingCount, setPendingCount] = useState(0);

    async function refreshPendingCount() {
        if (!localStorage.getItem("token")) return;
        const reports = await getPendingForMe();
        setPendingCount(reports.length);
    }

    useEffect(() => {
        if (token) refreshPendingCount();
    }, [token]);

    async function login(username: string, password: string) {
        const res = await apiLogin(username, password);
        localStorage.setItem("token", res.token);
        setToken(res.token);
    }

    async function register(username: string, password: string, displayName: string) {
        const res = await apiRegister(username, password, displayName);
        localStorage.setItem("token", res.token);
        setToken(res.token);
    }

    function logout() {
        localStorage.removeItem("token");
        setToken(null);
        setPendingCount(0);
    }

    return (
        <AuthContext.Provider
            value={{ token, isAuthenticated: !!token, pendingCount, refreshPendingCount, login, register, logout }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used within AuthProvider");
    return ctx;
}