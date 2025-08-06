// src/pages/ReceiptPage.jsx
import React, { useEffect, useState } from 'react';
import { useLocation,useNavigate } from 'react-router-dom';
import './ReceiptPage.css';

const ReceiptPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { sessionId, tableNumber } = location.state || {};

    const [token, setToken] = useState('');
    const [summary, setSummary] = useState(null);
    const [error, setError] = useState('');

    const fetchSummary = async () => {
        try {
            // First login (or reuse existing token logic if you have it)
            const loginRes = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: 'admin@example.com', password: 'password' }),
            });

            if (!loginRes.ok) throw new Error('Login failed');
            const { token } = await loginRes.json();
            setToken(token);

            const res = await fetch(`http://localhost:8080/sessions/${sessionId}/checkout-summary`, {
                headers: { Authorization: `Bearer ${token}` }
            });

            if (!res.ok) throw new Error('Failed to fetch receipt');

            const data = await res.json();
            setSummary(data);

        } catch (err) {
            console.error(err);
            setError(err.message);
        }
    };
    const confirmEndSession = async () => {
        try {
            const res = await fetch(`http://localhost:8080/sessions/${tableNumber}/end`, {
                method: 'PUT',
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (!res.ok) throw new Error('Failed to end session');

            const result = await res.json();
            console.log('Session ended:', result);
            navigate('/server');
        } catch (err) {
            console.error(err);
        }
    };

    const handlePrintAndGoBack = () => {
       confirmEndSession();
        navigate('/server');

    };



    useEffect(() => {


        fetchSummary();
    }, [sessionId]);

    if (error) return <div className="receipt-container">Error: {error}</div>;
    if (!summary) return <div className="receipt-container">Loading...</div>;

    return (
        <div className="receipt-container">
            <h1>🧾 Receipt</h1>
            <p><strong>Table:</strong> {summary.tableNumber}</p>
            <p><strong>Session ID:</strong> {summary.sessionId}</p>
            <p><strong>Total Orders:</strong> {summary.totalOrders}</p>
            <p><strong>Total Items:</strong> {summary.totalItemOrdered}</p>

            <hr />

            <h2>Items Ordered</h2>
            <ul className="receipt-items">
                {summary.items.map(item => (
                    <li key={item.itemId} className="receipt-item">
                        <span>{item.itemName}</span>
                        <span>x{item.totalQuantity}</span>
                        <span>${item.totalPrice.toFixed(2)}</span>
                    </li>
                ))}
            </ul>

            <hr />

            <h3 className="receipt-total">Total: ${summary.totalAmont.toFixed(2)}</h3>
            <button className="receipt-print-btn" onClick={handlePrintAndGoBack}>
                🖨️ Print & Return to Tables
            </button>

        </div>


    );
};

export default ReceiptPage;
