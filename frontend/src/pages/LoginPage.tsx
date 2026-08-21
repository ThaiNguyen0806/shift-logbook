import { useState } from "react";
import type { FormEvent } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const { login } = useAuth();
    const navigate = useNavigate();

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setError("");
        try {
            await login(username, password);
            navigate("/");
        } catch {
            setError("Invalid username or password");
        }
    }

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h1>Log in</h1>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Username</label>
                        <input value={username} onChange={(e) => setUsername(e.target.value)} />
                    </div>
                    <div>
                        <label>Password</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    {error && <p className="error">{error}</p>}
                    <button type="submit">Log in</button>
                </form>
                <p className="auth-switch">
                    Need an account? <Link to="/register">Sign up</Link>
                </p>
            </div>
        </div>
    );
}