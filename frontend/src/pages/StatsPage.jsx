import React, { useEffect, useState } from 'react';
import ApiService from '../services/ApiService';
import './StatsPage.css';

const MAX_ROWS = 5;

const StatsPage = () => {
  const [revenueByDate, setRevenueByDate] = useState({});
  const [revenueByMenuItem, setRevenueByMenuItem] = useState({});
  const [mostOrderedItems, setMostOrderedItems] = useState([]);
  const [averageRevenueByDate, setAverageRevenueByDate] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAllRevenueDates, setShowAllRevenueDates] = useState(false);
  const [showAllMenuItems, setShowAllMenuItems] = useState(false);
  const [showAllMostOrdered, setShowAllMostOrdered] = useState(false);
  const [showAllAvgRevenue, setShowAllAvgRevenue] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    setLoading(true);
    Promise.all([
      ApiService.getTotalRevenueByDate(token),
      ApiService.getTotalRevenueByMenuItem(token),
      ApiService.getMostOrderedItems(token),
      ApiService.getAverageSessionRevenueByDate(token)
    ])
      .then(([revDate, revMenu, mostOrdered, avgRev]) => {
        setRevenueByDate(revDate);
        setRevenueByMenuItem(revMenu);
        setMostOrderedItems(mostOrdered);
        setAverageRevenueByDate(avgRev);
        setLoading(false);
      })
      .catch(e => {
        setError(e.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <div className="stats-loading">Loading stats...</div>;
  if (error) return <div className="stats-error">Error: {error}</div>;

  // Sort and slice helpers
  const sortedRevenueDates = Object.entries(revenueByDate)
    .sort((a, b) => new Date(b[0]) - new Date(a[0]));
  const sortedAvgRevenue = [...averageRevenueByDate].sort((a, b) => new Date(b.date) - new Date(a.date));
  const sortedMenuItems = Object.entries(revenueByMenuItem).sort((a, b) => b[1] - a[1]);
  const sortedMostOrdered = [...mostOrderedItems].sort((a, b) => b.totalQuantity - a.totalQuantity);

  return (
    <div className="stats-dashboard-container">
      <h1>📊 Statistics Dashboard</h1>
      <div className="stats-dashboard-grid">
        {/* Total Revenue by Date */}
        <div className="stats-card">
          <h2>Total Revenue by Date</h2>
          <table className="stats-table">
            <thead>
              <tr><th>Date</th><th>Revenue</th></tr>
            </thead>
            <tbody>
              {(showAllRevenueDates ? sortedRevenueDates : sortedRevenueDates.slice(0, MAX_ROWS)).map(([date, revenue]) => (
                <tr key={date}><td>{date}</td><td>${revenue.toFixed(2)}</td></tr>
              ))}
            </tbody>
          </table>
          {sortedRevenueDates.length > MAX_ROWS && (
            <button className="stats-show-btn" onClick={() => setShowAllRevenueDates(v => !v)}>
              {showAllRevenueDates ? 'Show Less' : 'Show Full List'}
            </button>
          )}
        </div>
        {/* Total Revenue by Menu Item */}
        <div className="stats-card">
          <h2>Total Revenue by Menu Item</h2>
          <table className="stats-table">
            <thead>
              <tr><th>Menu Item</th><th>Revenue</th></tr>
            </thead>
            <tbody>
              {(showAllMenuItems ? sortedMenuItems : sortedMenuItems.slice(0, MAX_ROWS)).map(([item, revenue]) => (
                <tr key={item}><td>{item}</td><td>${revenue.toFixed(2)}</td></tr>
              ))}
            </tbody>
          </table>
          {sortedMenuItems.length > MAX_ROWS && (
            <button className="stats-show-btn" onClick={() => setShowAllMenuItems(v => !v)}>
              {showAllMenuItems ? 'Show Less' : 'Show Full List'}
            </button>
          )}
        </div>
        {/* Most Ordered Items */}
        <div className="stats-card">
          <h2>Most Ordered Items</h2>
          <table className="stats-table">
            <thead>
              <tr><th>Item</th><th>Order Count</th></tr>
            </thead>
            <tbody>
              {(showAllMostOrdered ? sortedMostOrdered : sortedMostOrdered.slice(0, MAX_ROWS)).map(item => (
                <tr key={item.name}>
                  <td>{item.name}</td>
                  <td>{item.totalQuantity}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {sortedMostOrdered.length > MAX_ROWS && (
            <button className="stats-show-btn" onClick={() => setShowAllMostOrdered(v => !v)}>
              {showAllMostOrdered ? 'Show Less' : 'Show Full List'}
            </button>
          )}
        </div>
        {/* Average Session Revenue by Date */}
        <div className="stats-card">
          <h2>Average Session Revenue by Date</h2>
          <table className="stats-table">
            <thead>
              <tr><th>Date</th><th>Average Revenue</th></tr>
            </thead>
            <tbody>
              {(showAllAvgRevenue ? sortedAvgRevenue : sortedAvgRevenue.slice(0, MAX_ROWS)).map(row => (
                <tr key={row.date}>
                  <td>{row.date}</td>
                  <td>${row.averageRevenue.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {sortedAvgRevenue.length > MAX_ROWS && (
            <button className="stats-show-btn" onClick={() => setShowAllAvgRevenue(v => !v)}>
              {showAllAvgRevenue ? 'Show Less' : 'Show Full List'}
            </button>
          )}
        </div>
      </div>
      <button
        className="stats-back-btn"
        onClick={() => window.location.href = '/admin'}
        style={{ margin: '36px auto 0', display: 'block', minHeight: '48px', fontSize: '1.02rem', fontWeight: 600, background: 'linear-gradient(90deg, #64748b 0%, #1e293b 100%)', color: '#fff', borderRadius: '18px', border: 'none', boxShadow: '0 2px 8px rgba(100,116,139,0.10)', transition: 'background 0.18s, color 0.18s, box-shadow 0.18s', padding: '0 32px', cursor: 'pointer' }}
        onMouseOver={e => e.currentTarget.style.background = 'linear-gradient(90deg, #1e293b 0%, #64748b 100%)'}
        onMouseOut={e => e.currentTarget.style.background = 'linear-gradient(90deg, #64748b 0%, #1e293b 100%)'}
      >
        ⬅️ Back
      </button>
    </div>
  );
};

export default StatsPage;
