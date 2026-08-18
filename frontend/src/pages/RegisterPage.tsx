import { useState, FormEvent } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function RegisterPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [displayName, setDisplayName] = useState("");
    const [error, setError] = useState("");
    const { register } = useAuth();
    const navigate = useNavigate();

    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        setError("");
        try {
            await register(username, password, displayName);
            navigate("/");
        } catch (err: any) {
            setError(err.response?.data?.message ?? "Registration failed");
        }
    }

    return (
        <div className="auth-page">
            <div className="auth-card">
                <h1>Create account</h1>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>Display name</label>
                        <input value={displayName} onChange={(e) => setDisplayName(e.target.value)} />
                    </div>
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
                    <button type="submit">Create account</button>
                </form>
                <p className="auth-switch">
                    Already have an account? <Link to="/login">Log in</Link>
                </p>
            </div>
        </div>
    );
}