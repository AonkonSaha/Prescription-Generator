import React, { useEffect, useState } from "react";
import Navbar from "../navbar-footer/Navbar";
import { useNavigate } from "react-router-dom";

function PrescriptionList() {
  const [prescriptions, setPrescriptions] = useState([]);
  const [filteredPrescriptions, setFilteredPrescriptions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [currentPage, setCurrentPage] = useState(1);

  const prescriptionsPerPage = 10;

  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    fetch(`${baseURL}/api/prescription/get/all/recent/month`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to fetch prescriptions");
        }
        return res.json();
      })
      .then((data) => {
        setPrescriptions(data.prescriptions);
        setFilteredPrescriptions(data.prescriptions);
        setLoading(false);
        setCurrentPage(1); // reset page on new data
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, [baseURL, token]);

  // Filter prescriptions when startDate or endDate changes
  useEffect(() => {
    if (startDate && endDate) {
      setLoading(true);
      fetch(`${baseURL}/api/prescription/get/all/${startDate}/${endDate}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          if (!res.ok) {
            throw new Error("Failed to fetch prescriptions for date range");
          }
          return res.json();
        })
        .then((data) => {
          setPrescriptions(data.prescriptions);
          setFilteredPrescriptions(data.prescriptions);
          setLoading(false);
          setCurrentPage(1); // reset page on new data
        })
        .catch((err) => {
          setError(err.message);
          setLoading(false);
        });
    } else {
      setLoading(true);
      fetch(`${baseURL}/api/prescription/get/all/recent/month`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          if (!res.ok) {
            throw new Error("Failed to fetch recent prescriptions");
          }
          return res.json();
        })
        .then((data) => {
          setPrescriptions(data.prescriptions);
          setFilteredPrescriptions(data.prescriptions);
          setLoading(false);
          setCurrentPage(1); // reset page on new data
        })
        .catch((err) => {
          setError(err.message);
          setLoading(false);
        });
    }
  }, [startDate, endDate, baseURL, token]);

  // Pagination calculations
  const indexOfLastPrescription = currentPage * prescriptionsPerPage;
  const indexOfFirstPrescription = indexOfLastPrescription - prescriptionsPerPage;
  const currentPrescriptions = filteredPrescriptions.slice(
    indexOfFirstPrescription,
    indexOfLastPrescription
  );
  const totalPages = Math.ceil(filteredPrescriptions.length / prescriptionsPerPage);

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleUpdate = (id) => {
    navigate(`/prescription/edit/${id}`);
  };

  const handleDelete = (id) => {
    if (window.confirm("Are you sure you want to delete this prescription?")) {
      fetch(`${baseURL}/api/prescription/delete/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => {
          if (!res.ok) {
            throw new Error("Failed to delete prescription");
          }
          setPrescriptions((prev) => prev.filter((p) => p.id !== id));
          setFilteredPrescriptions((prev) => prev.filter((p) => p.id !== id));
        })
        .catch((err) => {
          setError(err.message);
        });
    }
  };

  return (
    <>
      <Navbar />
      <div style={styles.container}>
        <h2 style={styles.title}>Prescription List</h2>

        {/* Date Filter */}
        <div style={styles.filterContainer}>
          <label style={styles.filterLabel}>
            Start Date:
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
              style={styles.filterInput}
              max={endDate || ""}
            />
          </label>

          <label style={styles.filterLabel}>
            End Date:
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
              style={styles.filterInput}
              min={startDate || ""}
            />
          </label>

          <button
            onClick={() => {
              setStartDate("");
              setEndDate("");
            }}
            style={styles.clearBtn}
            title="Clear filter"
          >
            Clear Filter
          </button>
        </div>

        {loading ? (
          <p style={{ textAlign: "center" }}>Loading...</p>
        ) : error ? (
          <p style={{ color: "red", textAlign: "center" }}>{error}</p>
        ) : filteredPrescriptions.length === 0 ? (
          <p style={{ textAlign: "center", marginTop: "30px" }}>
            No prescriptions found for the selected date range.
          </p>
        ) : (
          <div style={styles.tableWrapper}>
            <table style={styles.table}>
              <thead>
                <tr style={styles.theadRow}>
                  <th style={styles.th}>Prescription Date</th>
                  <th style={styles.th}>Patient Name</th>
                  <th style={styles.th}>Age</th>
                  <th style={styles.th}>Gender</th>
                  <th style={styles.th}>Diagnosis</th>
                  <th style={styles.th}>Medicines</th>
                  <th style={styles.th}>Next Visit Date</th>
                  <th style={styles.th}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {currentPrescriptions.map((prescription, index) => (
                  <tr
                    key={index}
                    style={{
                      ...styles.tr,
                      ...(index % 2 === 0 ? styles.tbodyRowEven : styles.tbodyRowOdd),
                    }}
                  >
                    <td style={styles.td}>{prescription.prescriptionDate}</td>
                    <td style={styles.td}>{prescription.patientName}</td>
                    <td style={styles.td}>{prescription.patientAge}</td>
                    <td style={styles.td}>{prescription.patientGender}</td>
                    <td style={styles.td}>{prescription.diagnosis?.join(", ")}</td>
                    <td style={styles.td}>{prescription.medicines?.join(", ")}</td>
                    <td style={styles.td}>{prescription.nextVisitDate}</td>
                    <td style={styles.td}>
                      <div style={styles.actionButtons}>
                        <button
                          style={{ ...styles.button, ...styles.updateBtn }}
                          onClick={() => handleUpdate(prescription.id)}
                        >
                          Update
                        </button>
                        <button
                          style={{ ...styles.button, ...styles.deleteBtn }}
                          onClick={() => handleDelete(prescription.id)}
                        >
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

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
          </div>
        )}
      </div>
    </>
  );
}

// ✅ Styles
const styles = {
  container: {
    marginTop: "80px",
    padding: "20px",
    maxWidth: "1100px",
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
  filterContainer: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    gap: "20px",
    marginBottom: "25px",
    flexWrap: "wrap",
  },
  filterLabel: {
    fontWeight: "600",
    color: "#34495e",
    fontSize: "1rem",
    display: "flex",
    flexDirection: "column",
    gap: "6px",
  },
  filterInput: {
    padding: "6px 10px",
    fontSize: "1rem",
    borderRadius: "6px",
    border: "1px solid #ccc",
    minWidth: "150px",
    outline: "none",
    transition: "border-color 0.2s ease",
  },
  clearBtn: {
    backgroundColor: "#95a5a6",
    color: "#fff",
    border: "none",
    borderRadius: "6px",
    padding: "8px 14px",
    fontSize: "14px",
    cursor: "pointer",
    fontWeight: "600",
    alignSelf: "flex-end",
    height: "38px",
    transition: "background-color 0.3s ease",
  },
  clearBtnHover: {
    backgroundColor: "#7f8c8d",
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
    minWidth: "900px",
    border: "1px solid #ccc", // Outer border for the table
  },
  theadRow: {
    backgroundColor: "#2c3e50",
    color: "#ffffff",
    borderBottom: "2px solid #2980b9", // Stronger bottom border for header
  },
  th: {
    padding: "14px 16px",
    textAlign: "left",
    color: "#fff",
    fontWeight: "600",
    borderRight: "1px solid #ccc", // Vertical divider between columns
    borderBottom: "1px solid #ccc", // Bottom border to separate header from body
  },
  td: {
    padding: "14px 16px",
    borderRight: "1px solid #ddd", // vertical divider between columns
    borderBottom: "1px solid #eee", // horizontal divider between rows
  },
  tr: {
    transition: "background-color 0.3s ease",
    cursor: "default",
  },
  tbodyRowEven: {
    backgroundColor: "#fafafa",
  },
  tbodyRowOdd: {
    backgroundColor: "#fff",
  },
  actionButtons: {
    display: "flex",
    gap: "10px",
  },
  button: {
    padding: "8px 14px",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    fontSize: "14px",
    fontWeight: "500",
    transition: "background-color 0.3s ease, transform 0.2s ease",
  },
  updateBtn: {
    backgroundColor: "#3498db",
    color: "#fff",
  },
  deleteBtn: {
    backgroundColor: "#e74c3c",
    color: "#fff",
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

export default PrescriptionList;
