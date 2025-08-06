import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './TablesDashboard.css';

const TablesDashboard = () => {
    const [token, setToken] = useState('');
    const [tables, setTables] = useState([]);
    const [activeTableNumbers, setActiveTableNumbers] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [selectedTable, setSelectedTable] = useState(null);

    const navigate = useNavigate();

    const loginAndGetToken = async () => {
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email: 'admin@example.com', password: 'password' }),
        });
        const data = await response.json();
        setToken(data.token);
        return data.token;
    };

    const fetchTables = async (token) => {
        const response = await fetch('http://localhost:8080/sessions/tables', {
            headers: { Authorization: `Bearer ${token}` },
        });
        const data = await response.json();
        setTables(data);
    };

    const fetchActiveSessions = async (token) => {
        const response = await fetch('http://localhost:8080/sessions/active', {
            headers: { Authorization: `Bearer ${token}` },
        });
        const data = await response.json();
        const activeNumbers = data.map(session => session.tableNumber);
        setActiveTableNumbers(activeNumbers);
    };

    const startSession = async (tableNumber) => {
        try {
            const response = await fetch('http://localhost:8080/sessions/start', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ tableNumber })
            });

            if (!response.ok) throw new Error('Failed to start session');

            const newSession = await response.json();
            console.log('Session started:', newSession);

            // Save sessionId in localStorage
            localStorage.setItem(`sessionId-${tableNumber}`, newSession.id);

            // Navigate to order page
            navigate('/orderview', {
                state: { sessionId: newSession.id, tableNumber }
            });

        } catch (err) {
            console.error('Error starting session:', err);
        }
    };

    const handleCardClick = async (tableName, isActive) => {
        if (!isActive) {
            setSelectedTable(tableName);
            setShowModal(true);
        } else {
            try {
                const res = await fetch(`http://localhost:8080/sessions/active/${tableName}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                const session = await res.json();

                navigate('/orderview', {
                    state: { sessionId: session.id, tableNumber: session.tableNumber }
                });
            } catch (err) {
                console.error('Failed to fetch active session:', err);
            }
        }
    };

    useEffect(() => {
        const init = async () => {
            try {
                const jwtToken = await loginAndGetToken();
                await Promise.all([
                    fetchTables(jwtToken),
                    fetchActiveSessions(jwtToken)
                ]);
            } catch (err) {
                console.error('Initialization error:', err);
            }
        };

        init();
    }, []);

    const handleConfirmStart = () => {
        if (selectedTable) {
            startSession(selectedTable);
        }
        setShowModal(false);
        setSelectedTable(null);
    };

    const handleCancelStart = () => {
        setShowModal(false);
        setSelectedTable(null);
    };

    return (
        <div className="tables-container">
            <h2>Available Tables</h2>

            <div className="tables-grid">
                {tables.map((t, index) => {
                    const isActive = activeTableNumbers.includes(t.tableName);
                    return (
                        <div
                            className={`table-card ${isActive ? 'active' : ''}`}
                            key={index}
                            onClick={() => handleCardClick(t.tableName, isActive)}
                            style={{ cursor: 'pointer' }}
                        >
                            {t.tableName}
                        </div>
                    );
                })}
            </div>

            <div style={{ textAlign: 'center', marginTop: '30px' }}>
                <button
                    onClick={() => navigate('/')}
                    className="back-home-btn"
                >
                    Back to Homepage
                </button>
            </div>

            {showModal && (
                <div className="modal-overlay">
                    <div className="modal-box">
                        <p style={{ color: 'black' }}>
                            Start session for <strong>{selectedTable}</strong>?
                        </p>
                        <div className="modal-buttons">
                            <button className="yes-btn" onClick={handleConfirmStart}>Yes</button>
                            <button className="no-btn" onClick={handleCancelStart}>No</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default TablesDashboard;
