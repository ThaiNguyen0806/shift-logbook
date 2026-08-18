import { Link, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Layout() {
    const { logout } = useAuth();
    const navigate = useNavigate();

    function handleLogout() {
        logout();
        navigate("/login");
    }

    return (
        <div>
            <header className="app-header">
                <Link to="/" className="app-title">Shift Logbook</Link>
                <nav>
                    <Link to="/">Dashboard</Link>
                    <Link to="/reports/new">New Report</Link>
                    <button className="logout-btn" onClick={handleLogout}>
                        Log out
                    </button>
                </nav>
            </header>
            <main>
                <Outlet />
            </main>
        </div>
    );
}