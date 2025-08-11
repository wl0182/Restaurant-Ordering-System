import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './DashboardStyles.css';
import ApiService from '../services/ApiService';

const KitchenDashboard = () => {
    const [token, setToken] = useState('');
    const [queue, setQueue] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    const loginAndFetchQueue = async () => {
        try {
            const token = localStorage.getItem('authToken');
            setToken(token);
            const queueData = await ApiService.getKitchenQueue(token);
            setQueue(queueData);
        } catch (err) {
            setError(err.message);
        }
    };

    const markAsServed = async (orderItemID) => {
        try {
            await ApiService.markOrderItemAsServed(orderItemID, token);

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
