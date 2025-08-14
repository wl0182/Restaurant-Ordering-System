import React, { useState, useEffect } from 'react';
import ApiService from '../services/ApiService';
import './SessionSummaryPage.css';


const SessionSummaryPage = () => {
  const [date, setDate] = useState('');
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [expandedSessionId, setExpandedSessionId] = useState(null);
  const [expandedSessionDetails, setExpandedSessionDetails] = useState(null);

  // Helper to get today's date in yyyy-MM-dd format
  const getToday = () => {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = String(today.getMonth() + 1).padStart(2, '0');
    const dd = String(today.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  };

  useEffect(() => {
    const todayStr = getToday();
    setDate(todayStr);
    const fetchTodaySessions = async () => {
      setLoading(true);
      setError('');
      try {
        const token = localStorage.getItem('authToken');
        const data = await ApiService.getSessionsByDate(token, todayStr);
        setSessions(data);
      } catch (err) {
        setError(err.message || 'Failed to fetch sessions');
      }
      setLoading(false);
    };
    fetchTodaySessions();
  }, []);

  const handleFetch = async () => {
    setLoading(true);
    setError('');
    try {
      const token = localStorage.getItem('authToken');
      const data = await ApiService.getSessionsByDate(token, date);
      setSessions(data);
    } catch (err) {
      setError(err.message || 'Failed to fetch sessions');
    }
    setLoading(false);
  };

  const handleSessionClick = async (sessionId) => {
    if (expandedSessionId === sessionId) {
      setExpandedSessionId(null);
      setExpandedSessionDetails(null);
      return;
    }
    setLoading(true);
    setError('');
    try {
      const token = localStorage.getItem('authToken');
      const details = await ApiService.getSessionById(token, sessionId);
      setExpandedSessionId(sessionId);
      setExpandedSessionDetails(details);
    } catch (err) {
      setError(err.message || 'Failed to fetch session details');
    }
    setLoading(false);
  };

  return (
    <div className="stats-container">
      <h1>Session Summaries by Date</h1>
      <div className="stats-date-row">
        <input
          type="date"
          value={date}
          onChange={e => setDate(e.target.value)}
          className="stats-date-input"
        />
        <button onClick={handleFetch} className="stats-fetch-btn">Fetch</button>
      </div>
      {loading && <div>Loading...</div>}
      {error && <div className="stats-error">{error}</div>}
      {sessions.length > 0 && (
        <table className="stats-table">
          <thead>
            <tr>
              <th>Session ID</th>
              <th>Table Number</th>
              <th>Total Orders</th>
              <th>Total Items Ordered</th>
              <th>Total Amount</th>
            </tr>
          </thead>
          <tbody>
            {sessions.map(session => (
              <React.Fragment key={session.sessionId}>
                <tr>
                  <td>
                    <button
                      className="stats-session-id-btn"
                      onClick={() => handleSessionClick(session.sessionId)}
                      style={{ cursor: 'pointer', color: '#1e3a8a', background: 'none', border: 'none', fontWeight: 'bold' }}
                    >
                      {session.sessionId}
                    </button>
                  </td>
                  <td>{session.tableNumber}</td>
                  <td>{session.totalOrders}</td>
                  <td>{session.totalItemOrdered}</td>
                  <td>{session.totalAmont?.toFixed(2)}</td>
                </tr>
                {expandedSessionId === session.sessionId && (
                  <tr>
                    <td colSpan={5}>
                      <div className="stats-session-details" style={{ background: '#232526', borderRadius: 8, padding: 16, margin: '12px 0' }}>
                        <div><strong>Session ID:</strong> {session.sessionId}</div>
                        <div><strong>Table Number:</strong> {session.tableNumber}</div>
                        <div><strong>Total Orders:</strong> {session.totalOrders}</div>
                        <div><strong>Total Items Ordered:</strong> {session.totalItemOrdered}</div>
                        <div><strong>Total Amount:</strong> {session.totalAmont?.toFixed(2)}</div>
                        {session.items && session.items.length > 0 && (
                          <div style={{ marginTop: 8 }}>
                            <strong>Items:</strong>
                            <table style={{ width: '100%', marginTop: 8, background: '#1e293b', borderRadius: 6 }}>
                              <thead>
                                <tr>
                                  <th>Item ID</th>
                                  <th>Item Name</th>
                                  <th>Order ID</th>
                                  <th>Quantity</th>
                                  <th>Served</th>
                                  <th>Total Price</th>
                                </tr>
                              </thead>
                              <tbody>
                                {session.items.map(item => (
                                  <tr key={item.itemId}>
                                    <td>{item.itemId}</td>
                                    <td>{item.itemName}</td>
                                    <td>{item.orderId}</td>
                                    <td>{item.totalQuantity}</td>
                                    <td>{item.served ? 'Yes' : 'No'}</td>
                                    <td>{item.totalPrice?.toFixed(2)}</td>
                                  </tr>
                                ))}
                              </tbody>
                            </table>
                          </div>
                        )}
                      </div>
                    </td>
                  </tr>
                )}
              </React.Fragment>
            ))}
          </tbody>
        </table>
      )}
      {sessions.length === 0 && !loading && !error && (
        <div>No sessions found for this date.</div>
      )}
      <button className="stats-back-btn" onClick={() => window.location.href = '/admin'}>⬅️ Back to Admin</button>
    </div>
  );
};

export default SessionSummaryPage;
