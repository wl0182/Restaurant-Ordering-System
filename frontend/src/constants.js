// src/constants.js

// Route paths
export const ROUTES = {
  LOGIN: '/login',
  REGISTER: '/register',
  HOME: '/',
  KITCHEN: '/kitchen',
  SERVER: '/server',
  ADMIN: '/admin',
  ORDER_VIEW: '/orderView',
  MENU: '/menu',
  RECEIPT: '/receipt',
  ADMIN_MENU: '/admin/menu',
  ADMIN_STATS: '/admin/stats',
  ADMIN_STAFF: '/admin/staff',
  ADMIN_SESSIONS: '/admin/sessions',
};

// Local storage keys
export const LOCAL_STORAGE_KEYS = {
  AUTH_TOKEN: 'authToken',
};

// API base URL
export const API_BASE = 'http://localhost:8080';

// Button labels
export const BUTTON_LABELS = {
  BACK: '⬅️ Back',
  BACK_TO_HOME: 'Back to Homepage',
  BACK_TO_ADMIN: '⬅️ Back to Admin',
  LOGIN: 'Login',
  REGISTER: 'Register',
  ADD_STAFF: 'Add Staff',
  ADD_ITEM: 'Add New Item',
  CANCEL: 'Cancel',
  SUBMIT: 'Submit',
  FETCH: 'Fetch',
  CONFIRM_ORDER: '✅ Confirm Order',
  PRINT_RETURN: '🖨️ Print & Return to Tables',
  YES: 'Yes',
  NO: 'No',
};

// Page titles
export const PAGE_TITLES = {
  HOME: 'Welcome',
  LOGIN: 'Login',
  REGISTER: 'Register',
  ADMIN: 'Admin Dashboard',
  MENU_MANAGEMENT: 'Menu Management',
  STAFF_MANAGEMENT: 'Staff Management',
  STATS: '📊 Statistics Dashboard',
  SESSION_SUMMARY: 'Session Summaries by Date',
  KITCHEN: 'Kitchen Dashboard',
  TABLES: 'Available Tables',
  RECEIPT: '🧾 Receipt',
};

// Icon constants
export const ICONS = {
  ADMIN: '🛠️',
  MENU: '🍽️',
  STAFF: '👥',
  STATS: '📊',
  SESSIONS: '🗂️',
  AI: '🤖',
  REGISTER: '📝',
  ORDER_PREVIEW: '🧺',
  RECEIPT: '🧾',
};

// Other constants
export const OTHER = {
  MAX_STATS_ROWS: 5,
};
