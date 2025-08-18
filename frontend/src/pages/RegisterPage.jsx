import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ApiService from '../services/ApiService';
import './HomePage.css';

const RegisterPage = () => {
    const [form, setForm] = useState({
        name: '',
        email: '',
        phone: '',
        role: 'ROLE_USER',
        password: '',
        confirmPassword: '',
        employeeId: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    const handleChange = e => {
        setForm({ ...form, [e.target.name]: e.target.value });
        setError('');
        setSuccess('');
    };

    const handleSubmit = async e => {
        e.preventDefault();
        setError('');
        setSuccess('');
        try {
            await ApiService.register({
                email: form.email,
                password: form.password,
                name: form.name,
                role: form.role,
                phone: form.phone,
                confirmPassword: form.confirmPassword,
                employeeId: form.employeeId
            });
            setSuccess('Registration successful! You can now log in.');
            setTimeout(() => navigate('/login'), 1200);
        } catch (err) {
            setError(err.message || 'Invalid Registration Details, Employee ID do not match email');
        }
    };

    return (
        <div className="home-container">
            <div className="home-icon">📝</div>
            <h1 className="home-heading">Register</h1>
            <form className="home-links" onSubmit={handleSubmit} style={{ flexDirection: 'column', gap: 16 }} autoComplete="off" spellCheck="false">
                <input
                    type="text"
                    name="name"
                    placeholder="Full Name"
                    value={form.name}
                    onChange={handleChange}
                    required
                    style={{ padding: 10, fontSize: 16 }}
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={form.email}
                    autoComplete="new-email"
                    onChange={handleChange}
                    required
                    style={{ padding: 10, fontSize: 16 }}
                />
                <input
                    type="tel"
                    name="phone"
                    placeholder="Phone Number"
                    value={form.phone}
                    onChange={handleChange}
                    required
                    style={{ padding: 10, fontSize: 16 }}
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={form.password}
                    autoComplete="new-password"
                    onChange={handleChange}
                    required
                    style={{ padding: 10, fontSize: 16 }}
                />
                <input
                    type="password"
                    name="confirmPassword"
                    placeholder="Confirm Password"
                    value={form.confirmPassword}
                    autoComplete="new-password"
                    onChange={handleChange}
                    required
                    style={{ padding: 10, fontSize: 16 }}
                />
                <input
                    type="text"
                    name="employeeId"
                    placeholder="Employee ID"
                    value={form.employeeId}
                    onChange={handleChange}
                    required
                    style={{ padding: 10, fontSize: 16 }}
                />
                <button className="home-button" type="submit">Register</button>
                {error && <div style={{ color: 'red', marginTop: 8 }}>{error}</div>}
                {success && <div style={{ color: '#43e97b', marginTop: 8 }}>{success}</div>}
            </form>
            <div style={{ marginTop: 18 }}>
                <button className="home-button" style={{ background: 'none', color: '#60a5fa', border: 'none', boxShadow: 'none', fontWeight: 400 }} onClick={() => navigate('/login')}>
                    Already have an account? Login
                </button>
            </div>
        </div>
    );
};

export default RegisterPage;
