import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './MenuView.css';
import ApiService from '../services/ApiService';

const MenuView = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { sessionId, tableNumber } = location.state || {};

    const [token, setToken] = useState('');
    const [menuItems, setMenuItems] = useState([]);
    const [quantities, setQuantities] = useState({});
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const { token } = await ApiService.login('admin@example.com', 'password');
                setToken(token);
                const menu = await ApiService.getAvailableMenuItems(token);
                setMenuItems(menu);
            } catch (err) {
                console.error(err);
                setError(err.message);
            }
        };

        fetchData();
    }, []);

    const handleIncrease = (itemId) => {
        setQuantities(prev => ({
            ...prev,
            [itemId]: (prev[itemId] || 0) + 1
        }));
    };

    const handleDecrease = (itemId) => {
        setQuantities(prev => {
            const current = prev[itemId] || 0;
            if (current <= 0) return prev;
            return { ...prev, [itemId]: current - 1 };
        });
    };

    const handlePlaceOrder = async () => {
        const selectedItems = Object.entries(quantities)
            .filter(([_, qty]) => qty > 0)
            .map(([id, qty]) => ({
                menuItemId: parseInt(id),
                quantity: parseInt(qty)
            }));

        if (selectedItems.length === 0) {
            alert('Please select at least one item.');
            return;
        }

        try {
            await ApiService.placeOrder(token, sessionId, selectedItems);

            navigate('/orderview', {
                state: { sessionId, tableNumber }
            });

        } catch (err) {
            console.error(err);
            alert('Failed to place order.');
        }
    };

    const groupedItems = menuItems.reduce((acc, item) => {
        acc[item.category] = acc[item.category] || [];
        acc[item.category].push(item);
        return acc;
    }, {});

    const selectedItems = Object.entries(quantities)
        .filter(([_, qty]) => qty > 0)
        .map(([id, qty]) => {
            const item = menuItems.find(m => m.id === parseInt(id));
            return {
                menuItemId: item.id,
                name: item.name,
                quantity: qty,
                total: item.price * qty
            };
        });
    const totalPrice = selectedItems.reduce((sum, item) => sum + item.total, 0);

    return (
        <div className="menu-container">
            <div className="menu-items">
                {Object.entries(groupedItems).map(([category, items]) => (
                    <div key={category} className="menu-category">
                        <h2 className="menu-category-title">{category}</h2>
                        <div className="menu-grid">
                            {items.map(item => (
                                <div key={item.id} className="menu-item-card">
                                    <div className="menu-item-name">{item.name}</div>
                                    <div className="menu-item-price">${item.price.toFixed(2)}</div>
                                    <div className="quantity-control">
                                        <button className="quantity-btn" onClick={() => handleDecrease(item.id)}>-</button>
                                        <div className="quantity-display">{quantities[item.id] || 0}</div>
                                        <button className="quantity-btn" onClick={() => handleIncrease(item.id)}>+</button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>

            <div className="menu-cart">
                <h3>🧺 Order Preview</h3>
                {selectedItems.length === 0 ? (
                    <div className="empty-cart-text">No items selected</div>
                ) : (
                    <>
                        {selectedItems.map(item => (
                            <div key={item.menuItemId} className="cart-item">
                                {item.name} x{item.quantity} — ${item.total.toFixed(2)}
                            </div>
                        ))}
                        <div className="cart-total">
                            <strong>Total: ${totalPrice.toFixed(2)}</strong>
                        </div>
                    </>

                )}

                {selectedItems.length > 0 && (
                    <button className="place-order-btn" onClick={handlePlaceOrder}>
                        ✅ Confirm Order
                    </button>
                )}
            </div>
        </div>
    );
};

export default MenuView;
