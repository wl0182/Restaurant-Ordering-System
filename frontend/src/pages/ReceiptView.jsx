// src/pages/ReceiptPage.jsx
import React, { useEffect, useState } from 'react';
import { useLocation,useNavigate } from 'react-router-dom';
import './ReceiptPage.css';
import ApiService from '../services/ApiService';

const ReceiptPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { sessionId, tableNumber } = location.state || {};

    const [token, setToken] = useState('');
    const [summary, setSummary] = useState(null);
    const [error, setError] = useState('');

    const fetchSummary = async () => {
        try {
            const { token } = await ApiService.login('admin@example.com', 'password');
            setToken(token);
            const data = await ApiService.getCheckoutSummary(token, sessionId);
            setSummary(data);
        } catch (err) {
            console.error(err);
            setError(err.message);
        }
    };
    const confirmEndSession = async () => {
        try {
            await ApiService.endSession(token, tableNumber);
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
