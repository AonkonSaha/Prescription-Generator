import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);
  const [profileOpen, setProfileOpen] = useState(false);
  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";
  const token = localStorage.getItem("token");

const handleLogout = async () => {

  try {
    const response = await fetch(`${baseURL}/api/auth/v1/logout`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      localStorage.removeItem("token");
      navigate("/login");
    } else {
      alert("Logout Failed: "+ await response.text());

      console.error("Logout failed:", await response.text());
    }
  } catch (error) {
    alert("Logout Failed: "+error);
    console.error("Logout error:", error);
  }
};


  return (
    <nav style={{ ...styles.navbar, ...(menuOpen ? styles.expandedNavbar : {}) }}>
      <div style={styles.logo}>Prescription Generator</div>

      <div style={{ ...styles.navLinks, ...(menuOpen ? styles.showMenu : {}) }}>
        <Link to="/" style={styles.link} onClick={() => setMenuOpen(false)}>Home</Link>
        <Link to="/prescription/generate" style={styles.link} onClick={() => setMenuOpen(false)}>Create</Link>
        <Link to="/prescriptions" style={styles.link} onClick={() => setMenuOpen(false)}>Prescriptions</Link>
        <Link to="/reports" style={styles.link} onClick={() => setMenuOpen(false)}>Reports</Link>
        <Link to="/covid/statistics" style={styles.link} onClick={() => setMenuOpen(false)}>ThirdPartyApi</Link>
      </div>

      <div style={styles.profileContainer}>
        <div style={styles.profile} onClick={() => setProfileOpen(!profileOpen)}>
          <span style={styles.profileIcon}>👨‍💼</span>
        </div>

        {profileOpen && (
          <div style={styles.dropdown}>
            <Link to="/profile" style={styles.dropdownItem} onClick={() => setProfileOpen(false)}>Profile</Link>
            <button style={styles.dropdownItem} onClick={handleLogout}>Logout</button>
          </div>
        )}
      </div>

      <button style={styles.menuButton} onClick={() => setMenuOpen(!menuOpen)}>
        ☰
      </button>
    </nav>
  );
}

const styles = {
  navbar: {
    position: "fixed",
    top: 0,
    left: 0,
    right: 0,
    height: "50px",
    backgroundColor: "#2c3e50",
    color: "#fff",
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    padding: "0 16px",
    zIndex: 1000,
  },
  expandedNavbar: {
    flexWrap: "wrap",
    height: "auto",
    paddingBottom: "10px",
  },
  logo: {
    fontSize: "18px",
    fontWeight: "600",
    whiteSpace: "nowrap",
  },
  navLinks: {
    display: "flex",
    gap: "15px",
    alignItems: "center",
  },
  link: {
    color: "#fff",
    textDecoration: "none",
    fontSize: "15px",
  },
  menuButton: {
    display: "none", // can enable for mobile later
    background: "none",
    border: "none",
    color: "#fff",
    fontSize: "24px",
    cursor: "pointer",
  },
  profileContainer: {
    position: "relative",
  },
  profile: {
    cursor: "pointer",
    backgroundColor: "#34495e",
    borderRadius: "50%",
    width: "32px",
    height: "32px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    transition: "background 0.3s",
  },
  profileIcon: {
    fontSize: "16px",
    color: "#fff",
  },
  dropdown: {
    position: "absolute",
    top: "40px",
    right: 0,
    backgroundColor: "#fff",
    color: "#333",
    border: "1px solid #ccc",
    borderRadius: "6px",
    overflow: "hidden",
    zIndex: 1000,
    boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
  },
  dropdownItem: {
    display: "block",
    padding: "10px 15px",
    width: "100%",
    background: "none",
    border: "none",
    textAlign: "left",
    cursor: "pointer",
    textDecoration: "none",
    color: "#333",
    fontSize: "14px",
  },
  showMenu: {
    flexDirection: "column",
    position: "absolute",
    top: "50px",
    left: 0,
    width: "100%",
    backgroundColor: "#2c3e50",
    padding: "10px 0",
    zIndex: 999,
  },
};

export default Navbar;
