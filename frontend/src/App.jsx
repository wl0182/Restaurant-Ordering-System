import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import KitchenDashboard from './pages/KitchenDashboard';
import TablesDashboard from "./pages/TablesDashboard.jsx";
import OrderView from "./pages/OrderView.jsx"
import MenuView from "./pages/MenuView.jsx";
import ReceiptView from "./pages/ReceiptView.jsx";



// const KitchenDashboard = () => <h2>Kitchen Dashboard</h2>;
//const ServerDashboard = () => <h2>Server Dashboard</h2>;
const AdminDashboard = () => <h2>Admin Dashboard</h2>;


function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/kitchen" element={<KitchenDashboard />} />
                <Route path="/server" element={<TablesDashboard />} />
                <Route path="/admin" element={<AdminDashboard />} />
                <Route path="/orderView" element={<OrderView />} />
                <Route path="/menu" element={<MenuView />} />
                <Route path="/receipt" element={<ReceiptView />} />
            </Routes>
        </Router>
    );
}

export default App;

