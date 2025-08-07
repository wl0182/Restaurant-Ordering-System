import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './OrderView.css';
import ApiService from '../services/ApiService';

const OrderView = () => {
    const location = useLocation();
    const navigate = useNavigate();

    const { sessionId, tableNumber } = location.state || {};

    const [token, setToken] = useState('');
    const [servedItems, setServedItems] = useState([]);
    const [unservedItems, setUnservedItems] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [total, setTotal] = useState(0.0);

    useEffect(() => {
        console.log('OrderView loaded with:');
        console.log('Session ID:', sessionId);
        console.log('Table Number:', tableNumber);
    }, []);

    // Step 1: Get token
    useEffect(() => {
        setToken(localStorage.getItem('authToken'));
    }, []);

    // Step 2: Fetch served + unserved items
    const fetchItems = async (jwtToken) => {
        const served = await ApiService.getServedItems(jwtToken, sessionId);
        const unserved = await ApiService.getUnservedItems(jwtToken, sessionId);
        setServedItems(served);
        setUnservedItems(unserved);
    };
    // fetch total
    const fetchTotal = async (jwtToken) => {
        // Use getCheckoutSummary to get totalAmont
        const summary = await ApiService.getCheckoutSummary(jwtToken, sessionId);
        setTotal(summary.totalAmont);
        return summary.totalAmont;
    };

    useEffect(() => {
        const init = async () => {
            if (!sessionId) {
                console.error('No session ID found');
                return;
            }
            try {
                const jwt = localStorage.getItem('authToken');
                await fetchItems(jwt);
                await fetchTotal(jwt);
            } catch (err) {
                console.error(err);
            }
        };

        init();
    }, [sessionId]);

    const handlePlaceOrder = () => {
        navigate('/menu', {
            state: { sessionId, tableNumber }
        });
    };

    const handleEndSession = () => {
        setShowModal(true);
        navigate('/receipt', {
            state: { sessionId, tableNumber }
        });
    };

    const confirmEndSession = async () => {
        try {
            await ApiService.endSession(token, tableNumber);
            navigate('/server');
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="order-view-container">
            <h2>Table: {tableNumber}</h2>

            <div className="order-section">
                <h3>🧾 Unserved Items</h3>
                {unservedItems.length === 0 ? (
                    <p>No unserved items.</p>
                ) : (
                    <ul>
                        {unservedItems.map(item => (
                            <li key={item.itemId}>
                                {item.name} x{item.quantity} - ${item.totalPrice.toFixed(2)}
                            </li>
                        ))}
                    </ul>
                )}
            </div>

            <div className="order-section">
                <h3>✅ Served Items</h3>
                {servedItems.length === 0 ? (
                    <p>No served items.</p>
                ) : (
                    <ul>
                        {servedItems.map(item => (
                            <li key={item.itemId}>
                                {item.name} x{item.quantity} - ${item.totalPrice.toFixed(2)} ✅
                            </li>
                        ))}
                    </ul>
                )}
            </div>
            <div className="order-section">
                <h3>Total : ${total} </h3>
            </div>

            <div className="button-row">
                <button onClick={() => navigate('/server')}>⬅ Return</button>
                <button onClick={handlePlaceOrder}>➕ Place Order</button>
                <button onClick={handleEndSession} className="danger">❌ End Session</button>
            </div>

            {showModal && (
                <div className="modal-overlay">
                    <div className="modal-box">
                        <p>Are you sure you want to end session for table {tableNumber}?</p>
                        <div className="modal-buttons">
                            <button onClick={confirmEndSession}>Yes</button>
                            <button onClick={() => setShowModal(false)}>No</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default OrderView;
