// src/services/ApiService.js

const API_BASE = 'http://localhost:8080';

class ApiService {
    // Auth
    static async login(email, password) {
        const res = await fetch(`${API_BASE}/api/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (!res.ok) throw new Error('Login failed');
        return res.json();
    }

    //register
    static async register(email, password, name, role ,phone,confirmPassword) {
       // Check if passwords match
        if (password !== confirmPassword) {
            throw new Error('Passwords do not match');
        }
        const res = await fetch(`${API_BASE}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password, name, role, phone })
        });
        if (!res.ok) throw new Error('Registration failed');
        return res.json();
    }

    // Kitchen Queue
    static async getKitchenQueue(token) {
        const res = await fetch(`${API_BASE}/orders/kitchen/queue`, {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }
        });
        if (!res.ok) throw new Error('Failed to fetch kitchen queue');
        return res.json();
    }

    // Mark order item as served
    static async markOrderItemAsServed(orderItemID, token) {
        const res = await fetch(`${API_BASE}/orders/orderItem/${orderItemID}/serve`, {
            method: 'POST',
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to mark as served');
        return res.json();
    }

    // Fetch available menu items
    static async getAvailableMenuItems(token) {
        const res = await fetch(`${API_BASE}/api/menu-items/available`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch menu');
        return res.json();
    }

    // Place order
    static async placeOrder(token, tableSessionId, items) {
        const res = await fetch(`${API_BASE}/orders`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({ tableSessionId, items })
        });
        if (!res.ok) throw new Error('Order failed');
        return res.json();
    }

    // Get all menu items
    static async getAllMenuItems(token) {
        const res = await fetch(`${API_BASE}/api/menu-items`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch all menu items');
        return res.json();
    }

    // Get menu item by ID
    static async getMenuItemById(token, id) {
        const res = await fetch(`${API_BASE}/api/menu-items/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch menu item by ID');
        return res.json();
    }

    // Update menu item availability
    static async updateMenuItemAvailability(token, id) {
        const res = await fetch(`${API_BASE}/api/menu-items/${id}`, {
            method: 'PUT',
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to update menu item availability');
        return res.json();
    }

    // Create menu item
    static async createMenuItem(token, menuItem) {
        const res = await fetch(`${API_BASE}/api/menu-items/add`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify(menuItem)
        });
        if (!res.ok) throw new Error('Failed to create menu item');
        return res.json();
    }

    // Get all tables
    static async getAllTables(token) {
        const res = await fetch(`${API_BASE}/sessions/tables`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch tables');
        return res.json();
    }

    // Get active sessions
    static async getActiveSessions(token) {
        const res = await fetch(`${API_BASE}/sessions/active`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch active sessions');
        return res.json();
    }

    // Get active session by table number
    static async getActiveSessionByTable(token, tableNumber) {
        const res = await fetch(`${API_BASE}/sessions/active/${tableNumber}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch active session by table');
        return res.json();
    }

    // Start session
    static async startSession(token, tableNumber) {
        const res = await fetch(`${API_BASE}/sessions/start`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({ tableNumber })
        });
        if (!res.ok) throw new Error('Failed to start session');
        return res.json();
    }

    // End session
    static async endSession(token, tableNumber) {
        const res = await fetch(`${API_BASE}/sessions/${tableNumber}/end`, {
            method: 'PUT',
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to end session');
        return res.json();
    }

    // Get session by ID
    static async getSessionById(token, id) {
        const res = await fetch(`${API_BASE}/sessions/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch session by ID');
        return res.json();
    }

    // Get item summary for session
    static async getItemSummary(token, id) {
        const res = await fetch(`${API_BASE}/sessions/${id}/item-summary`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch item summary');
        return res.json();
    }

    // Get all item names for session
    static async getAllItemNames(token, id) {
        const res = await fetch(`${API_BASE}/sessions/${id}/item-names`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch item names');
        return res.json();
    }

    // Get session summary for checkout
    static async getCheckoutSummary(token, id) {
        const res = await fetch(`${API_BASE}/sessions/${id}/checkout-summary`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch checkout summary');
        return res.json();
    }



    // Get order by ID
    static async getOrderById(token, id) {
        const res = await fetch(`${API_BASE}/orders/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch order by ID');
        return res.json();
    }

    // Get orders by session ID
    static async getOrdersBySession(token, id) {
        const res = await fetch(`${API_BASE}/orders/sessions/${id}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch orders by session');
        return res.json();
    }

    // Get served items by session
    static async getServedItems(token, id) {
        const res = await fetch(`${API_BASE}/orders/sessions/${id}/served`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch served items');
        return res.json();
    }

    // Get unserved items by session
    static async getUnservedItems(token, id) {
        const res = await fetch(`${API_BASE}/orders/sessions/${id}/unserved`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch unserved items');
        return res.json();
    }

    // Mark order as served
    static async markOrderAsServed(token, id) {
        const res = await fetch(`${API_BASE}/orders/${id}/serve`, {
            method: 'POST',
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to mark order as served');
        return res.json();
    }


    // Get menu items by category
    static async getMenuItemsByCategory(token, category) {
        const res = await fetch(`${API_BASE}/api/menu-items/category/${category}`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch menu items by category');
        return res.json();
    }
}

export default ApiService;

