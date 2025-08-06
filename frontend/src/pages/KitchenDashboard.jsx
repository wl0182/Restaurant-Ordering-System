import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './DashboardStyles.css';

const KitchenDashboard = () => {
    const [token, setToken] = useState('');
    const [queue, setQueue] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    const loginAndFetchQueue = async () => {
        try {
            // Step 1: Generate Token
            const loginRes = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    email: 'admin@example.com',
                    password: 'password',
                }),
            });

            if (!loginRes.ok) throw new Error('Login failed');// if the gen token api is not returning a 200 response or similar we throw Error excpetion

            const { token } = await loginRes.json(); //
            setToken(token);

            // Step 2: Fetch Kitchen Queue
            const queueRes = await fetch('http://localhost:8080/orders/kitchen/queue', {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!queueRes.ok) throw new Error('Failed to fetch kitchen queue');

            const queueData = await queueRes.json();
            setQueue(queueData);
        } catch (err) {
            setError(err.message);
        }
    };

    const markAsServed = async (orderItemID) => {
        try {
            console.log("Marking as served:", orderItemID);
            console.log("Using token:", token);
            const res = await fetch(`http://localhost:8080/orders/orderItem/${orderItemID}/serve`, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (!res.ok) throw new Error('Failed to mark as served');

            const result = await res.json();
            console.log(result);

            // Refresh queue after update
            loginAndFetchQueue();
        } catch (err) {
            setError(err.message);
        }
    };

    useEffect(() => {
        loginAndFetchQueue();
    }, []);

    return (
        <div className="dashboard-container">
            <h1>Kitchen Dashboard</h1>

            {error && <p className="error-text">{error}</p>}

            {queue.length === 0 ? (
                <p>No orders in the kitchen queue.</p>
            ) : (
                <table>
                    <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Order Item ID</th>
                        <th>Table</th>
                        <th>Item</th>
                        <th>Quantity</th>
                        <th>Served</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    {queue.map((item, index) => (
                        <tr key={index}>
                            <td>{item.orderId}</td>
                            <td>{item.orderItemId}</td>
                            <td>{item.tableNumber}</td>
                            <td>{item.itemName}</td>
                            <td>{item.quantity}</td>
                            <td>
                                {item.served ? (
                                    <span className="status-served">Yes</span>
                                ) : (
                                    <span className="status-unserved">No</span>
                                )}
                            </td>
                            <td>
                                {!item.served && (
                                    <button
                                        className="button serve"
                                        onClick={() => markAsServed(item.orderItemId)}
                                    >
                                        Mark as Served
                                    </button>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>


            )}

            <div style={{ textAlign: 'center', marginTop: '30px' }}>
                <button
                    onClick={() => navigate('/')}
                    className="back-home-btn"
                >
                    Back to Homepage
                </button>
            </div>
        </div>

    );
};

export default KitchenDashboard;
