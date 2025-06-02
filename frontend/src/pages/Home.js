import React from "react";
import Navbar from "../navbar-footer/Navbar";

function Home() {
  return (
    <div>
      <Navbar />
      <div style={styles.container}>
        <h1 style={styles.heading}>Welcome to Prescription Generator</h1>
        <p style={styles.subheading}>Easily manage and generate prescriptions for your patients.</p>
        <div style={styles.cardContainer}>
          <div style={styles.card}>
            <h3>📄 Create Prescription</h3>
            <p>Quickly create and print prescriptions for your patients.</p>
          </div>
          <div style={styles.card}>
            <h3>📋 View Prescriptions</h3>
            <p>Access the list of all previously generated prescriptions.</p>
          </div>
          <div style={styles.card}>
            <h3>📊 Reports</h3>
            <p>Track and analyze prescription statistics and summaries.</p>
          </div>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: {
    padding: "30px 20px",
    textAlign: "center",
    backgroundColor: "#f4f6f8",
    minHeight: "calc(100vh - 60px)",
  },
  heading: {
    fontSize: "32px",
    marginBottom: "10px",
    color: "#2c3e50",
  },
  subheading: {
    fontSize: "18px",
    marginBottom: "30px",
    color: "#555",
  },
  cardContainer: {
    display: "flex",
    justifyContent: "center",
    gap: "20px",
    flexWrap: "wrap",
  },
  card: {
    backgroundColor: "#fff",
    padding: "20px",
    borderRadius: "10px",
    boxShadow: "0 2px 10px rgba(0, 0, 0, 0.1)",
    width: "280px",
    textAlign: "left",
  },
};

export default Home;
