import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './TablesDashboard.css';
import ApiService from '../services/ApiService';

const TablesDashboard = () => {
    const [token, setToken] = useState('');
    const [tables, setTables] = useState([]);
    const [activeTableNumbers, setActiveTableNumbers] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [selectedTable, setSelectedTable] = useState(null);

    const navigate = useNavigate();

    const loginAndGetToken = async () => {
        const { token } = await ApiService.login('admin@example.com', 'password');
        setToken(token);
        return token;
    };

    const fetchTables = async (token) => {
        const data = await ApiService.getTables(token);
        setTables(data);
    };

    const fetchActiveSessions = async (token) => {
        const data = await ApiService.getActiveSessions(token);
        const activeNumbers = data.map(session => session.tableNumber);
        setActiveTableNumbers(activeNumbers);
    };

    const startSession = async (tableNumber) => {
        try {
            const newSession = await ApiService.startSession(token, tableNumber);
            localStorage.setItem(`sessionId-${tableNumber}`, newSession.id);
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
                const session = await ApiService.getActiveSessionByTable(token, tableName);
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
