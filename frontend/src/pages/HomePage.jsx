import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './HomePage.css';
import { LOCAL_STORAGE_KEYS } from '../constants';

// Helper function to get user role from JWT token
function getUserRole() {
    const token = localStorage.getItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN);
    if (!token) return null;

    try {
        // Decode JWT payload
        const payload = JSON.parse(atob(token.split('.')[1]));

        // Debug: Log the entire payload to see what's actually in the JWT
        console.log('JWT Payload:', payload);

        // Check for authorities array (Spring Security format)
        if (payload.authorities && Array.isArray(payload.authorities)) {
            console.log('Authorities found:', payload.authorities);
            // Find if ROLE_ADMIN exists in authorities
            const hasAdminRole = payload.authorities.some(auth =>
                auth === 'ROLE_ADMIN' || auth.authority === 'ROLE_ADMIN'
            );
            return hasAdminRole ? 'ROLE_ADMIN' : 'ROLE_USER';
        }

        // Fallback to direct role property
        console.log('No authorities array, checking role property:', payload.role);
        return payload.role || 'ROLE_USER';
    } catch (error) {
        console.error('Error decoding JWT:', error);
        return null;
    }
}

const HomePage = () => {
    const [userRole, setUserRole] = useState(getUserRole());

    // Update role when component mounts or token changes
    useEffect(() => {
        setUserRole(getUserRole());
    }, []);

    const isAdmin = userRole === 'ROLE_ADMIN';

    return (
        <div className="home-container">
            <h1 className="home-heading">Welcome</h1>
            <p className="home-subtext">Please select an option below:</p>
            <div className="home-links">
                <Link to="/kitchen" className="home-button">Kitchen Dashboard</Link>
                <Link to="/server" className="home-button">Server Dashboard</Link>

                {isAdmin ? (
                    <Link to="/admin" className="home-button">Admin Dashboard</Link>
                ) : (
                    <button
                        className="home-button"
                        disabled
                        style={{
                            backgroundColor: '#e5e7eb',
                            color: '#9ca3af',
                            cursor: 'not-allowed',
                            opacity: 0.6
                        }}
                        title="Admin access required"
                    >
                        Admin Dashboard
                    </button>
                )}
            </div>
        </div>
    );
};

export default HomePage;
