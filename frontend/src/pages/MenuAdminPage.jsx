import React, { useEffect, useState } from 'react';
import ApiService from '../services/ApiService';
import { useNavigate } from 'react-router-dom';
import './MenuAdminPage.css';

const MenuAdminPage = () => {
  const [menuItems, setMenuItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showAdd, setShowAdd] = useState(false);
  const [newItem, setNewItem] = useState({ name: '', price: '', category: '', imageUrl: '', available: true });
  const [refresh, setRefresh] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState('');
  const [pendingToggle, setPendingToggle] = useState(null);
  const token = localStorage.getItem('authToken');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMenu = async () => {
      setLoading(true);
      try {
        const items = await ApiService.getAllMenuItems(token);
        setMenuItems(items);
        setError('');
      } catch (e) {
        setError('Failed to fetch menu items');
      }
      setLoading(false);
    };
    fetchMenu();
  }, [token, refresh]);

  const handleInputChange = e => {
    setNewItem({ ...newItem, [e.target.name]: e.target.value });
  };

  const handleAddItem = async e => {
    e.preventDefault();
    try {
      await ApiService.createMenuItem(token, { ...newItem, price: parseFloat(newItem.price), available: Boolean(newItem.available) });
      setShowAdd(false);
      setNewItem({ name: '', price: '', category: '', imageUrl: '', available: true });
      setRefresh(r => !r);
    } catch (e) {
      setError('Failed to add menu item');
    }
  };

  // Toggle availability for a menu item
  const handleToggleAvailability = async (id) => {
    try {
      await ApiService.updateMenuItemAvailability(token, id);
      setRefresh(r => !r);
    } catch (e) {
      setError('Failed to update availability');
    }
  };

  const handleSwitchClick = (item) => {
    if (item.available) {
      setPendingToggle(item);
    } else {
      handleToggleAvailability(item.id);
    }
  };

  const handleConfirmToggle = async () => {
    if (pendingToggle) {
      await handleToggleAvailability(pendingToggle.id);
      setPendingToggle(null);
    }
  };

  const handleCancelToggle = () => {
    setPendingToggle(null);
  };

  // Get unique categories for dropdown
  const categoryOptions = Array.from(new Set(menuItems.map(item => item.category))).filter(Boolean);

  return (
    <div className="menu-admin-container">
      <h2>Menu Management</h2>
      <div className="menu-admin-btn-row">
        <button onClick={() => navigate('/admin')}>⬅ Back</button>
        <button onClick={() => setShowAdd(s => !s)} className="menu-admin-add-btn">{showAdd ? 'Cancel' : 'Add New Item'}</button>
      </div>
      <div className="menu-admin-filter-row">
        <label htmlFor="categoryFilter">Filter by Category: </label>
        <select id="categoryFilter" value={categoryFilter} onChange={e => setCategoryFilter(e.target.value)}>
          <option value="">All</option>
          {categoryOptions.map(cat => (
            <option key={cat} value={cat}>{cat}</option>
          ))}
        </select>
      </div>
      {showAdd && (
        <form onSubmit={handleAddItem} className="menu-admin-form">
          <input name="name" placeholder="Name" value={newItem.name} onChange={handleInputChange} required />
          <input name="price" type="number" step="0.01" placeholder="Price" value={newItem.price} onChange={handleInputChange} required />
          <select name="category" value={newItem.category} onChange={handleInputChange} required>
            <option value="">Select Category</option>
            {categoryOptions.map(cat => (
              <option key={cat} value={cat}>{cat}</option>
            ))}
          </select>
          <input name="imageUrl" placeholder="Image URL" value={newItem.imageUrl} onChange={handleInputChange} />
          <select name="available" value={newItem.available} onChange={handleInputChange}>
            <option value={true}>Available</option>
            <option value={false}>Unavailable</option>
          </select>
          <button type="submit">Add Item</button>
        </form>
      )}
      {loading ? (
        <p>Loading...</p>
      ) : error ? (
        <p className="menu-admin-error">{error}</p>
      ) : (
        <table className="menu-admin-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Price</th>
              <th>Category</th>
              <th>Available</th>
            </tr>
          </thead>
          <tbody>
            {menuItems.filter(item => !categoryFilter || item.category === categoryFilter).map(item => (
              <tr key={item.id}>
                <td>{item.name}</td>
                <td>${item.price.toFixed(2)}</td>
                <td>{item.category}</td>
                <td>
                  <label className="switch">
                    <input
                      type="checkbox"
                      checked={item.available}
                      onChange={() => handleSwitchClick(item)}
                    />
                    <span className="slider round"></span>
                  </label>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      {pendingToggle && (
        <div className="menu-modal-overlay">
          <div className="menu-modal-box">
            <p>Are you sure you want to make item unavailable?</p>
            <div className="menu-modal-buttons">
              <button className="danger" onClick={handleConfirmToggle}>Yes</button>
              <button onClick={handleCancelToggle}>No</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MenuAdminPage;
