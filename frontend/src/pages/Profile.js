import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Navbar from '../navbar-footer/Navbar';

const Profile = () => {
  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";
  const token = localStorage.getItem('token');

  const [profile, setProfile] = useState({
    name: '',
    email: '',
    imageUrl: '',
  });

  const [imageFile, setImageFile] = useState(null);
  const [passwords, setPasswords] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  });

  const [message, setMessage] = useState('');

  useEffect(() => {
    axios.get(`${baseURL}/api/profile`, {
      headers: { Authorization: `Bearer ${token}` },
    }).then(res => setProfile(res.data))
      .catch(err => console.error("Profile load failed", err));
  }, [baseURL, token]);

  const handleChange = (e) => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  const handleImageChange = (e) => {
    setImageFile(e.target.files[0]);
  };

  const handleProfileSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('name', profile.name);
    formData.append('email', profile.email);
    if (imageFile) formData.append('image', imageFile);

    axios.put(`${baseURL}/api/profile`, formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'multipart/form-data',
      }
    }).then(() => setMessage('Profile updated successfully.'))
      .catch(() => setMessage('Failed to update profile.'));
  };

  const handlePasswordChange = (e) => {
    e.preventDefault();
    if (passwords.newPassword !== passwords.confirmPassword) {
      setMessage('Passwords do not match.');
      return;
    }

    axios.put(`${baseURL}/api/profile/password`, passwords, {
      headers: { Authorization: `Bearer ${token}` },
    }).then(() => setMessage('Password changed successfully.'))
      .catch(() => setMessage('Failed to change password.'));
  };

  return (
    <>
    <Navbar/>
    <div style={styles.container}>
      <h2 style={styles.title}>Profile Settings</h2>

      {message && <p style={styles.message}>{message}</p>}

      <form onSubmit={handleProfileSubmit} style={styles.form}>
        <div style={styles.formGroup}>
          <label>Name</label>
          <input type="text" name="name" value={profile.name} onChange={handleChange} style={styles.input} />
        </div>

        <div style={styles.formGroup}>
          <label>Email</label>
          <input type="email" name="email" value={profile.email} onChange={handleChange} style={styles.input} />
        </div>

        <div style={styles.formGroup}>
          <label>Profile Image</label>
          <input type="file" onChange={handleImageChange} style={styles.input} />
          {profile.imageUrl && (
            <img src={profile.imageUrl} alt="Profile" style={styles.imagePreview} />
          )}
        </div>

        <button type="submit" style={styles.button}>Update Profile</button>
      </form>

      <form onSubmit={handlePasswordChange} style={{ ...styles.form, marginTop: '40px' }}>
        <h3>Change Password</h3>

        <div style={styles.formGroup}>
          <label>Old Password</label>
          <input
            type="password"
            name="oldPassword"
            value={passwords.oldPassword}
            onChange={(e) => setPasswords({ ...passwords, oldPassword: e.target.value })}
            style={styles.input}
          />
        </div>

        <div style={styles.formGroup}>
          <label>New Password</label>
          <input
            type="password"
            name="newPassword"
            value={passwords.newPassword}
            onChange={(e) => setPasswords({ ...passwords, newPassword: e.target.value })}
            style={styles.input}
          />
        </div>

        <div style={styles.formGroup}>
          <label>Confirm Password</label>
          <input
            type="password"
            name="confirmPassword"
            value={passwords.confirmPassword}
            onChange={(e) => setPasswords({ ...passwords, confirmPassword: e.target.value })}
            style={styles.input}
          />
        </div>

        <button type="submit" style={styles.button}>Change Password</button>
      </form>
    </div>
    </>
    
  );
};

const styles = {
  container: {
    maxWidth: '600px',
    margin: '60px auto',
    padding: '20px',
    border: '1px solid #ddd',
    borderRadius: '10px',
    backgroundColor: '#fdfdfd',
    fontFamily: 'Arial',
  },
  title: {
    textAlign: 'center',
    fontSize: '24px',
    marginBottom: '20px',
  },
  message: {
    color: 'green',
    textAlign: 'center',
    marginBottom: '20px',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
  },
  formGroup: {
    marginBottom: '16px',
  },
  input: {
    width: '100%',
    padding: '10px',
    marginTop: '6px',
    borderRadius: '6px',
    border: '1px solid #ccc',
  },
  button: {
    padding: '10px 16px',
    backgroundColor: '#4CAF50',
    color: 'white',
    fontWeight: 'bold',
    border: 'none',
    borderRadius: '6px',
    cursor: 'pointer',
  },
  imagePreview: {
    width: '100px',
    height: '100px',
    objectFit: 'cover',
    marginTop: '10px',
    borderRadius: '50%',
    border: '2px solid #ccc',
  },
};

export default Profile;
