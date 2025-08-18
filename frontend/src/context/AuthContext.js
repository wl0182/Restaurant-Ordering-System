import React, { createContext, useContext, useState } from 'react';
import { LOCAL_STORAGE_KEYS } from '../constants';

const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [token, setToken] = useState(localStorage.getItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN));
    const [role, setRole] = useState(getUserRole(token));

    function getUserRole(token) {
        if (!token) return null;
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            if (payload.authorities && Array.isArray(payload.authorities)) {
                return payload.authorities.includes('ROLE_ADMIN') ? 'ROLE_ADMIN' : 'ROLE_USER';
            }
            return payload.role || null;
        } catch {
            return null;
        }
    }

    function login(newToken) {
        localStorage.setItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN, newToken);
        setToken(newToken);
        setRole(getUserRole(newToken));
    }

    function logout() {
        localStorage.removeItem(LOCAL_STORAGE_KEYS.AUTH_TOKEN);
        setToken(null);
        setRole(null);
    }

    return (
        <AuthContext.Provider value={{ token, role, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    return useContext(AuthContext);
}

