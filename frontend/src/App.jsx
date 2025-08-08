import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import KitchenDashboard from './pages/KitchenDashboard';
import TablesDashboard from "./pages/TablesDashboard.jsx";
import OrderView from "./pages/OrderView.jsx"
import MenuView from "./pages/MenuView.jsx";
import ReceiptView from "./pages/ReceiptView.jsx";
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';



// const KitchenDashboard = () => <h2>Kitchen Dashboard</h2>;
//const ServerDashboard = () => <h2>Server Dashboard</h2>;
const AdminDashboard = () => <h2>Admin Dashboard</h2>;

// This component checks if the user is authenticated
function RequireAuth({ children }) {
    const token = localStorage.getItem('authToken');
    if (!token) {
        window.location.href = '/login';
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
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/" element={<RequireAuth><HomePage /></RequireAuth>} />
                <Route path="/kitchen" element={<RequireAuth><KitchenDashboard /></RequireAuth>} />
                <Route path="/server" element={<RequireAuth><TablesDashboard /></RequireAuth>} />
                <Route path="/admin" element={<RequireAuth><AdminDashboard /></RequireAuth>} />
                <Route path="/orderView" element={<RequireAuth><OrderView /></RequireAuth>} />
                <Route path="/menu" element={<RequireAuth><MenuView /></RequireAuth>} />
                <Route path="/receipt" element={<RequireAuth><ReceiptView /></RequireAuth>} />
            </Routes>
        </Router>
    );
}

export default App;
