import React, { useEffect, useState } from 'react';
import ApiService from '../services/ApiService';
import './StaffManagementPage.css';

const StaffManagementPage = () => {
  const [staffList, setStaffList] = useState([]);
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    employeeId: '',
    role: ''
  });
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);

  const fetchStaff = async () => {
    setLoading(true);
    setError('');
    try {
      const token = localStorage.getItem('authToken');
      const data = await ApiService.getAllStaff(token);
      setStaffList(data);
    } catch (e) {
      setError('Failed to fetch staff list');
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchStaff();
  }, []);

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
      const token = localStorage.getItem('authToken');
      // Ensure employeeId is sent as a number
      const staffData = {
        ...form,
        employeeId: Number(form.employeeId)
      };
      await ApiService.addStaff(token, staffData);
      console.log("Staff added successfully")
      setSuccess('Staff member added successfully!');
      setForm({ firstName: '', lastName: '', email: '', employeeId: '', role: '' });
      fetchStaff();
      setShowForm(false);
    } catch (err) {
      console.log('Failed to add staff')
      setError(err.message || 'Failed to add staff member');
    }
  };

  const handleAddClick = () => {
    setShowForm(true);
    setSuccess('');
    setError('');
  };

  const handleCancel = () => {
    setShowForm(false);
    setForm({ firstName: '', lastName: '', email: '', employeeId: '', role: '' });
    setSuccess('');
    setError('');
  };

  return (
    <div className="staff-admin-container">
      <h1>Staff Management</h1>
      <div className="staff-admin-btn-row">
        <button className="staff-admin-back-btn" onClick={() => window.location.href = '/admin'} style={{marginRight: 12}}>⬅️ Back</button>
        <button className="staff-admin-add-btn" onClick={handleAddClick} disabled={showForm}>Add Staff</button>
      </div>
      {showForm && (
        <form className="staff-admin-form" onSubmit={handleSubmit} autoComplete="off" spellCheck="false">
          <input type="text" name="firstName" placeholder="First Name" value={form.firstName} onChange={handleChange} required />
          <input type="text" name="lastName" placeholder="Last Name" value={form.lastName} onChange={handleChange} required />
          <input type="email" name="email" placeholder="Email" value={form.email} onChange={handleChange} required />
          <input type="text" name="employeeId" placeholder="Employee ID" value={form.employeeId} onChange={handleChange} required />
          <input type="text" name="role" placeholder="Role" value={form.role} onChange={handleChange} required />
          <button type="submit">Submit</button>
          <button type="button" onClick={handleCancel}>Cancel</button>
        </form>
      )}
      {success && <div className="staff-admin-success">{success}</div>}
      {error && <div className="staff-admin-error">{error}</div>}
      <h2 style={{marginBottom: 12}}>All Staff Members</h2>
      {loading ? (
        <div>Loading staff...</div>
      ) : staffList.length === 0 ? (
        <div>No staff found.</div>
      ) : (
        <table className="staff-admin-table">
          <thead>
            <tr>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Email</th>
              <th>Employee ID</th>
              <th>Role</th>
            </tr>
          </thead>
          <tbody>
            {staffList.map((staff, idx) => (
              <tr key={idx}>
                <td>{staff.firstName}</td>
                <td>{staff.lastName}</td>
                <td>{staff.email}</td>
                <td>{staff.employeeId}</td>
                <td>{staff.role}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default StaffManagementPage;
