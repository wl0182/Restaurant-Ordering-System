import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './AdminDashboard.module.css';

const quickLinks = [
  { label: 'Menu Management', path: '/admin/menu', icon: '🍽️', desc: 'Add, edit, or remove menu items.' },
  { label: 'Staff Management', path: '/admin/staff', icon: '👥', desc: 'Manage staff accounts and roles.' },
  { label: 'Stats', path: '/admin/stats', icon: '📊', desc: 'View sales and operational statistics.' },
  { label: 'Sessions Summaries', path: '/admin/sessions', icon: '🗂️', desc: 'Review all table sessions.' },
  { label: 'AI Insight', path: '/admin/ai', icon: '🤖', desc: 'Get AI-powered insights and chat.' },
];

const AdminDashboard = () => {
  const navigate = useNavigate();
  return (
    <div className={styles['admin-container']}>
      <div className={styles['admin-icon']}>🛠️</div>
      <h1 className={styles['admin-heading']}>Admin Dashboard</h1>
      <div className={styles['admin-subtext']}>
        Quick access to management and analytics tools
      </div>
      <div className={styles['admin-links']}>
        {quickLinks.map(link => (
          <button
            key={link.label}
            className={styles['admin-link-btn']}
            onClick={() => navigate(link.path)}
          >
            <span className={styles['admin-link-icon']}>{link.icon}</span>
            <span className={styles['admin-link-label']}>{link.label}</span>
            <span className={styles['admin-link-desc']}>{link.desc}</span>
          </button>
        ))}
      </div>
      <button
        className={styles['admin-link-btn'] + ' ' + styles['admin-back-btn']}
        onClick={() => navigate('/')}
      >
        ⬅️ Back to Home
      </button>
    </div>
  );
};

export default AdminDashboard;
