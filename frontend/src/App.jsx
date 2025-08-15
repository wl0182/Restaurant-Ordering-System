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
                <Route path={ROUTES.ADMIN} element={<RequireAuth><AdminDashboard /></RequireAuth>} />
                <Route path={ROUTES.ORDER_VIEW} element={<RequireAuth><OrderView /></RequireAuth>} />
                <Route path={ROUTES.MENU} element={<RequireAuth><MenuView /></RequireAuth>} />
                <Route path={ROUTES.RECEIPT} element={<RequireAuth><ReceiptView /></RequireAuth>} />
                <Route path={ROUTES.ADMIN_MENU} element={<RequireAuth><MenuAdminPage /></RequireAuth>} />
                <Route path={ROUTES.ADMIN_STATS} element={<RequireAuth><StatsPage /></RequireAuth>} />
                <Route path={ROUTES.ADMIN_STAFF} element={<RequireAuth><StaffManagementPage /></RequireAuth>} />
                <Route path={ROUTES.ADMIN_SESSIONS} element={<RequireAuth><SessionSummaryPage /></RequireAuth>} />
            </Routes>
        </Router>
    );
}

export default App;
