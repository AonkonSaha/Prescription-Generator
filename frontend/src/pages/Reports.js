import React, { useEffect, useState } from "react";
import axios from "axios";
import Navbar from "../navbar-footer/Navbar";

const Reports = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);

  const reportsPerPage = 4;

  const token = localStorage.getItem("token");
  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";

  useEffect(() => {
    setLoading(true);
    axios
      .get(`${baseURL}/api/prescription/v1/report`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        setReports(response.data);
        setLoading(false);
        setCurrentPage(1); // Reset to first page on new data load
      })
      .catch(() => {
        setError("Failed to load reports.");
        setLoading(false);
      });
  }, [token, baseURL]);

  // Pagination calculations
  const indexOfLastReport = currentPage * reportsPerPage;
  const indexOfFirstReport = indexOfLastReport - reportsPerPage;
  const currentReports = reports.slice(indexOfFirstReport, indexOfLastReport);
  const totalPages = Math.ceil(reports.length / reportsPerPage);

  const handleNextPage = () => {
    if (currentPage < totalPages) setCurrentPage(currentPage + 1);
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) setCurrentPage(currentPage - 1);
  };

  return (
    <>
      <Navbar />
      <div style={styles.container}>
        <h2 style={styles.title}>Prescription Report</h2>

        {loading ? (
          <p style={{ textAlign: "center" }}>Loading...</p>
        ) : error ? (
          <p style={{ color: "red", textAlign: "center" }}>{error}</p>
        ) : reports.length === 0 ? (
          <p style={{ textAlign: "center", marginTop: "30px" }}>
            No report data available.
          </p>
        ) : (
          <>
            <div style={styles.tableWrapper}>
              <table style={styles.table}>
                <thead>
                  <tr style={styles.theadRow}>
                    <th style={styles.th}>Date</th>
                    <th style={styles.th}>Prescription Count</th>
                  </tr>
                </thead>
                <tbody>
                  {currentReports.map((report, index) => (
                    <tr
                      key={index}
                      style={{
                        ...styles.tr,
                        ...(index % 2 === 0
                          ? styles.tbodyRowEven
                          : styles.tbodyRowOdd),
                      }}
                    >
                      <td style={styles.td}>{report.day}</td>
                      <td style={styles.td}>{report.count}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* Pagination Controls */}
            <div style={styles.paginationContainer}>
              <button
                onClick={handlePreviousPage}
                disabled={currentPage === 1}
                style={{
                  ...styles.paginationButton,
                  ...(currentPage === 1 && styles.disabledButton),
                }}
              >
                Previous
              </button>

              <span style={styles.pageIndicator}>
                Page {currentPage} of {totalPages}
              </span>

              <button
                onClick={handleNextPage}
                disabled={currentPage === totalPages}
                style={{
                  ...styles.paginationButton,
                  ...(currentPage === totalPages && styles.disabledButton),
                }}
              >
                Next
              </button>
            </div>
          </>
        )}
      </div>
    </>
  );
};

const styles = {
  container: {
    marginTop: "80px",
    padding: "20px",
    maxWidth: "900px",
    marginLeft: "auto",
    marginRight: "auto",
    fontFamily: "Segoe UI, Tahoma, Geneva, Verdana, sans-serif",
  },
  title: {
    textAlign: "center",
    fontSize: "2rem",
    color: "#2c3e50",
    marginBottom: "30px",
  },
  tableWrapper: {
    overflowX: "auto",
    boxShadow: "0 2px 10px rgba(0, 0, 0, 0.1)",
    borderRadius: "12px",
    backgroundColor: "#fff",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    fontSize: "16px",
    minWidth: "600px",
    border: "1px solid #ccc",
  },
  theadRow: {
    backgroundColor: "#2c3e50",
    color: "#ffffff",
  },
  th: {
    padding: "14px 16px",
    textAlign: "left",
    fontWeight: "600",
    borderRight: "1px solid #ccc",
    borderBottom: "1px solid #ccc",
    color: "#fff",
  },
  td: {
    padding: "14px 16px",
    borderRight: "1px solid #ddd",
    borderBottom: "1px solid #eee",
  },
  tr: {
    transition: "background-color 0.3s ease",
  },
  tbodyRowEven: {
    backgroundColor: "#fafafa",
  },
  tbodyRowOdd: {
    backgroundColor: "#fff",
  },
  paginationContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    marginTop: "20px",
    gap: "15px",
  },
  paginationButton: {
    padding: "8px 16px",
    fontSize: "14px",
    fontWeight: "600",
    borderRadius: "6px",
    border: "none",
    cursor: "pointer",
    backgroundColor: "#2c3e50",
    color: "#fff",
    transition: "opacity 0.3s ease",
  },
  disabledButton: {
    opacity: 0.5,
    cursor: "not-allowed",
  },
  pageIndicator: {
    fontSize: "14px",
    fontWeight: "600",
    color: "#2c3e50",
  },
};

export default Reports;
