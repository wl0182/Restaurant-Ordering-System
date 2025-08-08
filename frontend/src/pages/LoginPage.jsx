import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../services/ApiService';
import './HomePage.css';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const { token } = await ApiService.login(email, password);
            localStorage.setItem('authToken', token);
            navigate('/');
        } catch (err) {
            setError('Invalid credentials');
        }
    };

    return (
        <div className="home-container">
            <h1 className="home-heading">Login</h1>
            <form className="home-links" onSubmit={handleSubmit} style={{ flexDirection: 'column', gap: 16 }}>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    required
                    style={{ padding: 8, fontSize: 16 }}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                    style={{ padding: 8, fontSize: 16 }}
                />
                <button className="home-button" type="submit">Login</button>
                {error && <div style={{ color: 'red', marginTop: 8 }}>{error}</div>}
            </form>
            <div style={{ marginTop: 18 }}>
                <button
                    className="home-button"
                    style={{ background: 'none', color: '#60a5fa', border: 'none', boxShadow: 'none', fontWeight: 400 }}
                    onClick={() => navigate('/register')}
                >
                    Don't have an account? Register
                </button>
            </div>
        </div>
    );
};

export default LoginPage;
