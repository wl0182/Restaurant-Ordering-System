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
  const [editModal, setEditModal] = useState({ open: false, item: null, name: '', price: '', category: '' });
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

  // Open edit modal
  const openEditModal = (item) => {
    setEditModal({
      open: true,
      item,
      name: item.name,
      price: item.price,
      category: item.category
    });
  };

  // Close edit modal
  const closeEditModal = () => {
    setEditModal({ open: false, item: null, name: '', price: '', category: '' });
  };

  // Update name
  const handleUpdateName = async () => {
    try {
      await ApiService.updateMenuItemName(token, editModal.item.id, editModal.name);
      setRefresh(r => !r);
      closeEditModal();
    } catch (e) {
      setError('Failed to update name');
    }
  };

  // Update price
  const handleUpdatePrice = async () => {
    try {
      await ApiService.updateMenuItemPrice(token, editModal.item.id, editModal.price);
      setRefresh(r => !r);
      closeEditModal();
    } catch (e) {
      setError('Failed to update price');
    }
  };

  // Update category
  const handleUpdateCategory = async () => {
    try {
      await ApiService.updateMenuItemCategory(token, editModal.item.id, editModal.category);
      setRefresh(r => !r);
      closeEditModal();
    } catch (e) {
      setError('Failed to update category');
    }
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
                    <td onClick={() => openEditModal(item)} style={{ cursor: 'pointer', textDecoration: 'underline' }}>{item.name}</td>
                    <td onClick={() => openEditModal(item)} style={{ cursor: 'pointer', textDecoration: 'underline' }}>${item.price.toFixed(2)}</td>
                    <td onClick={() => openEditModal(item)} style={{ cursor: 'pointer', textDecoration: 'underline' }}>{item.category}</td>
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
        {editModal.open && (
            <div className="menu-modal-overlay">
              <div className="menu-edit-modal-box">
                <button className="menu-edit-modal-close" onClick={closeEditModal}>&times;</button>
                <h3>Edit Menu Item</h3>
                <div className="menu-edit-modal-row">
                  <label>Name:</label>
                  <input
                      type="text"
                      value={editModal.name}
                      onChange={e => setEditModal({ ...editModal, name: e.target.value })}
                  />
                  <button onClick={handleUpdateName}>Update</button>
                </div>
                <div className="menu-edit-modal-row">
                  <label>Price:</label>
                  <input
                      type="number"
                      step="0.01"
                      value={editModal.price}
                      onChange={e => setEditModal({ ...editModal, price: e.target.value })}
                  />
                  <button onClick={handleUpdatePrice}>Update</button>
                </div>
                <div className="menu-edit-modal-row">
                  <label>Category:</label>
                  <select
                      value={editModal.category}
                      onChange={e => setEditModal({ ...editModal, category: e.target.value })}
                  >
                    <option value="">Select Category</option>
                    {categoryOptions.map(cat => (
                        <option key={cat} value={cat}>{cat}</option>
                    ))}
                  </select>
                  <button onClick={handleUpdateCategory}>Update</button>
                </div>
              </div>
            </div>
        )}
      </div>
  );
};

export default MenuAdminPage;
