import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ROUTES, LOCAL_STORAGE_KEYS } from './constants';
import HomePage from './pages/HomePage';
import KitchenDashboard from './pages/KitchenDashboard';
import TablesDashboard from "./pages/TablesDashboard.jsx";
import OrderView from "./pages/OrderView.jsx"
import MenuView from "./pages/MenuView.jsx";
import ReceiptView from "./pages/ReceiptView.jsx";
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import AdminDashboard from './pages/AdminDashboard';
import MenuAdminPage from "./pages/MenuAdminPage.jsx";
import StatsPage from "./pages/StatsPage.jsx";
import StaffManagementPage from "./pages/StaffManagementPage.jsx";
import SessionSummaryPage from "./pages/SessionSummaryPage.jsx";





// This component checks if the user is authenticated
function RequireAuth({ children }) {
    const token = localStorage.getItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN);
    if (!token) {
        window.location.href = ROUTES.LOGIN;
        return null;
    }
    return children;
}

// This component checks if the user is an admin
function RequireAdmin({ children }) {
    const token = localStorage.getItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN);
    if (!token) {
        window.location.href = ROUTES.LOGIN;
        return null;
    }

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));

        // Check for admin role in authorities array or direct role property
        let isAdmin = false;
        if (payload.authorities && Array.isArray(payload.authorities)) {
            isAdmin = payload.authorities.some(auth =>
                auth === 'ROLE_ADMIN' || auth.authority === 'ROLE_ADMIN'
            );
        } else {
            isAdmin = payload.role === 'ROLE_ADMIN';
        }

        if (!isAdmin) {
            window.location.href = ROUTES.HOME;
            return null;
        }
    } catch (error) {
        console.error('Error checking admin role:', error);
        window.location.href = ROUTES.HOME;
        return null;
    }

    return children;
}

// Main App component with routing
// Adding RequireAuth to protect routes
function App() {
    return (
        <Router>
            <Routes>
                <Route path={ROUTES.LOGIN} element={<LoginPage />} />
                <Route path={ROUTES.REGISTER} element={<RegisterPage />} />
                <Route path={ROUTES.HOME} element={<RequireAuth><HomePage /></RequireAuth>} />
                <Route path={ROUTES.KITCHEN} element={<RequireAuth><KitchenDashboard /></RequireAuth>} />
                <Route path={ROUTES.SERVER} element={<RequireAuth><TablesDashboard /></RequireAuth>} />
                <Route path={ROUTES.ADMIN} element={<RequireAdmin><AdminDashboard /></RequireAdmin>} />
                <Route path={ROUTES.ORDER_VIEW} element={<RequireAuth><OrderView /></RequireAuth>} />
                <Route path={ROUTES.MENU} element={<RequireAuth><MenuView /></RequireAuth>} />
                <Route path={ROUTES.RECEIPT} element={<RequireAuth><ReceiptView /></RequireAuth>} />
                <Route path={ROUTES.ADMIN_MENU} element={<RequireAdmin><MenuAdminPage /></RequireAdmin>} />
                <Route path={ROUTES.ADMIN_STATS} element={<RequireAdmin><StatsPage /></RequireAdmin>} />
                <Route path={ROUTES.ADMIN_STAFF} element={<RequireAdmin><StaffManagementPage /></RequireAdmin>} />
                <Route path={ROUTES.ADMIN_SESSIONS} element={<RequireAdmin><SessionSummaryPage /></RequireAdmin>} />
            </Routes>
        </Router>
    );
}

export default App;
