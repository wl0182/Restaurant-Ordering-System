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

    // Add more methods as needed for your app...
    // Fetch served items
    static async getServedItems(sessionId, token) {
        const res = await fetch(`${API_BASE}/orders/sessions/${sessionId}/served`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch served items');
        return res.json();
    }

    // Fetch unserved items
    static async getUnservedItems(sessionId, token) {
        const res = await fetch(`${API_BASE}/orders/sessions/${sessionId}/unserved`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch unserved items');
        return res.json();
    }
    // Fetch checkout summary
    static async getCheckoutSummary(sessionId, token) {
        // Fetch the checkout summary for a session
        const res = await fetch(`${API_BASE}/sessions/${sessionId}/checkout-summary`, {
            // Use the sessionId to get the summary
            headers: {Authorization: `Bearer ${token}`}
            // Include the token for authorization
        });
        if (!res.ok) throw new Error('Failed to fetch checkout summary');
        return res.json();
    }
    // End session
    static async endSession(tableNumber, token) {
        // End a session for a specific table number
        const res = await fetch(`${API_BASE}/sessions/${tableNumber}/end`, {
            method: 'PUT',
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to end session');
        return res.json();
    }

    // Fetch active sessions
    static async getActiveSessions(token) {
        // Fetch all active sessions
        const res = await fetch(`${API_BASE}/sessions/active`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!res.ok) throw new Error('Failed to fetch active sessions');
        return res.json();
    }


}

export default ApiService;

