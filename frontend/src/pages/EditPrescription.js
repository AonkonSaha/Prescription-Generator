import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Navbar from "../navbar-footer/Navbar";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function EditPrescription() {
  const { id } = useParams();
  const navigate = useNavigate();
  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";
  const token = localStorage.getItem("token");
  const todayString = new Date().toISOString().split("T")[0];

  const [form, setForm] = useState({
    prescriptionDate: todayString,
    patientName: "",
    patientAge: "",
    patientGender: "",
    diagnosis: [""],
    medicines: [""],
    nextVisitDate: "",
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(true);
  const [fetchError, setFetchError] = useState(null);

  // Fetch prescription by id
  useEffect(() => {
    async function fetchPrescription() {
      try {
        const res = await fetch(`${baseURL}/api/prescription/get/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (!res.ok) throw new Error("Failed to fetch prescription");
        const data = await res.json();

        setForm({
          prescriptionDate: data.prescriptionDate || todayString,
          patientName: data.patientName || "",
          patientAge: data.patientAge?.toString() || "",
          patientGender: data.patientGender || "",
          diagnosis: data.diagnosis?.length ? data.diagnosis : [""],
          medicines: data.medicines?.length ? data.medicines : [""],
          nextVisitDate: data.nextVisitDate || "",
        });
        setLoading(false);
      } catch (error) {
        setFetchError(error.message);
        setLoading(false);
      }
    }
    fetchPrescription();
  }, [id, baseURL, token, todayString]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleDiagnosisChange = (index, value) => {
    const newDiagnosis = [...form.diagnosis];
    newDiagnosis[index] = value;
    setForm((prev) => ({ ...prev, diagnosis: newDiagnosis }));
  };

  const addDiagnosis = () => {
    setForm((prev) => ({ ...prev, diagnosis: [...prev.diagnosis, ""] }));
  };

  const removeDiagnosis = (index) => {
    if (form.diagnosis.length === 1) return;
    const newDiagnosis = form.diagnosis.filter((_, i) => i !== index);
    setForm((prev) => ({ ...prev, diagnosis: newDiagnosis }));
  };

  const handleMedicinesChange = (index, value) => {
    const newMedicines = [...form.medicines];
    newMedicines[index] = value;
    setForm((prev) => ({ ...prev, medicines: newMedicines }));
  };

  const addMedicine = () => {
    setForm((prev) => ({ ...prev, medicines: [...prev.medicines, ""] }));
  };

  const removeMedicine = (index) => {
    if (form.medicines.length === 1) return;
    const newMedicines = form.medicines.filter((_, i) => i !== index);
    setForm((prev) => ({ ...prev, medicines: newMedicines }));
  };

  const validate = () => {
    const newErrors = {};
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // if (!form.prescriptionDate) {
    //   newErrors.prescriptionDate = "Prescription Date is required";
    // } else {
    //   const prescDate = new Date(form.prescriptionDate);
    //   prescDate.setHours(0, 0, 0, 0);
    //   if (prescDate.getTime() !== today.getTime()) {
    //     newErrors.prescriptionDate = "Prescription Date must be today's date";
    //   }
    // }
    
    if (!form.patientName.trim()) {
      newErrors.patientName = "Patient Name is required";
    }

    if (!form.patientAge.trim()) {
      newErrors.patientAge = "Patient Age is required";
    } else if (!/^\d+$/.test(form.patientAge.trim())) {
      newErrors.patientAge = "Patient Age must be a number";
    } else if (
      parseInt(form.patientAge.trim()) <= 0 ||
      parseInt(form.patientAge.trim()) > 200
    ) {
      newErrors.patientAge = "Patient Age must be between 1 and 200";
    }

    if (!form.patientGender) {
      newErrors.patientGender = "Patient Gender is required";
    }

    if (!form.diagnosis.length || form.diagnosis.every((d) => !d.trim())) {
      newErrors.diagnosis = "At least one Diagnosis is required";
    } else {
      form.diagnosis.forEach((d, idx) => {
        if (!d.trim()) newErrors[`diagnosis_${idx}`] = "Diagnosis cannot be empty";
      });
    }

    if (!form.medicines.length || form.medicines.every((m) => !m.trim())) {
      newErrors.medicines = "At least one Medicine is required";
    } else {
      form.medicines.forEach((m, idx) => {
        if (!m.trim()) newErrors[`medicines_${idx}`] = "Medicine cannot be empty";
      });
    }

    if (!form.nextVisitDate) {
      newErrors.nextVisitDate = "Next Visit Date is required";
    } else {
      const nextDate = new Date(form.nextVisitDate);
      nextDate.setHours(0, 0, 0, 0);
      if (nextDate <= today) {
        newErrors.nextVisitDate = "Next Visit Date must be after today";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    try {
      const response = await fetch(`${baseURL}/api/prescription/update/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        const errorData = await response.json();
        toast.error("Failed to update: " + (errorData.message || response.statusText));
        return;
      }

      toast.success("Prescription updated successfully!");
      setTimeout(() => navigate("/prescriptions"), 1500);
    } catch (error) {
      toast.error("Network error: " + error.message);
    }
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <p style={{ textAlign: "center", marginTop: "80px" }}>Loading...</p>
      </>
    );
  }

  if (fetchError) {
    return (
      <>
        <Navbar />
        <p style={{ color: "red", textAlign: "center", marginTop: "80px" }}>
          Error: {fetchError}
        </p>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <ToastContainer position="top-right" autoClose={3000} />
      <div style={styles.container}>
        <h2>Edit Prescription</h2>
        <form onSubmit={handleSubmit} style={styles.form}>
          {/* Prescription Date */}
          <div style={styles.formGroup}>
            <label>Prescription Date *</label>
            <input
              type="date"
              name="prescriptionDate"
              value={form.prescriptionDate}
              onChange={handleChange}
              style={styles.input}
            />
            {errors.prescriptionDate && <span style={styles.error}>{errors.prescriptionDate}</span>}
          </div>

          {/* Patient Name */}
          <div style={styles.formGroup}>
            <label>Patient Name *</label>
            <input
              type="text"
              name="patientName"
              value={form.patientName}
              onChange={handleChange}
              style={styles.input}
            />
            {errors.patientName && <span style={styles.error}>{errors.patientName}</span>}
          </div>

          {/* Patient Age */}
          <div style={styles.formGroup}>
            <label>Patient Age *</label>
            <input
              type="text"
              name="patientAge"
              value={form.patientAge}
              onChange={handleChange}
              style={styles.input}
            />
            {errors.patientAge && <span style={styles.error}>{errors.patientAge}</span>}
          </div>

          {/* Gender */}
          <div style={styles.formGroup}>
            <label>Patient Gender *</label>
            <select
              name="patientGender"
              value={form.patientGender}
              onChange={handleChange}
              style={styles.input}
            >
              <option value="">-- Select Gender --</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Others">Others</option>
            </select>
            {errors.patientGender && <span style={styles.error}>{errors.patientGender}</span>}
          </div>

          {/* Diagnosis */}
          <div style={styles.formGroup}>
            <label>Diagnosis *</label>
            {form.diagnosis.map((d, idx) => (
              <div key={idx} style={{ display: "flex", marginBottom: "6px" }}>
                <input
                  type="text"
                  value={d}
                  onChange={(e) => handleDiagnosisChange(idx, e.target.value)}
                  style={{ ...styles.input, flex: 1 }}
                />
                <button
                  type="button"
                  onClick={() => removeDiagnosis(idx)}
                  disabled={form.diagnosis.length === 1}
                  style={styles.removeBtn}
                >
                  &times;
                </button>
              </div>
            ))}
            {errors.diagnosis && <span style={styles.error}>{errors.diagnosis}</span>}
            {form.diagnosis.map((_, idx) =>
              errors[`diagnosis_${idx}`] ? (
                <span key={`err_d_${idx}`} style={styles.error}>
                  {errors[`diagnosis_${idx}`]}
                </span>
              ) : null
            )}
            <button type="button" onClick={addDiagnosis} style={styles.addBtn}>
              + Add Diagnosis
            </button>
          </div>

          {/* Medicines */}
          <div style={styles.formGroup}>
            <label>Medicines *</label>
            {form.medicines.map((m, idx) => (
              <div key={idx} style={{ display: "flex", marginBottom: "6px" }}>
                <input
                  type="text"
                  value={m}
                  onChange={(e) => handleMedicinesChange(idx, e.target.value)}
                  style={{ ...styles.input, flex: 1 }}
                />
                <button
                  type="button"
                  onClick={() => removeMedicine(idx)}
                  disabled={form.medicines.length === 1}
                  style={styles.removeBtn}
                >
                  &times;
                </button>
              </div>
            ))}
            {errors.medicines && <span style={styles.error}>{errors.medicines}</span>}
            {form.medicines.map((_, idx) =>
              errors[`medicines_${idx}`] ? (
                <span key={`err_m_${idx}`} style={styles.error}>
                  {errors[`medicines_${idx}`]}
                </span>
              ) : null
            )}
            <button type="button" onClick={addMedicine} style={styles.addBtn}>
              + Add Medicine
            </button>
          </div>

          {/* Next Visit Date */}
          <div style={styles.formGroup}>
            <label>Next Visit Date *</label>
            <input
              type="date"
              name="nextVisitDate"
              value={form.nextVisitDate}
              onChange={handleChange}
              min={todayString}
              style={styles.input}
            />
            {errors.nextVisitDate && <span style={styles.error}>{errors.nextVisitDate}</span>}
          </div>

          <button type="submit" style={styles.submitButton}>
            Update Prescription
          </button>
        </form>
      </div>
    </>
  );
}

const styles = {
  container: {
    maxWidth: "650px",
    margin: "60px auto",
    backgroundColor: "#fff",
    padding: "30px",
    borderRadius: "8px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  },
  form: {
    display: "flex",
    flexDirection: "column",
  },
  formGroup: {
    marginBottom: "20px",
  },
  input: {
    width: "100%",
    padding: "8px 12px",
    fontSize: "1rem",
    borderRadius: "4px",
    border: "1px solid #ccc",
    outline: "none",
  },
  error: {
    color: "#d32f2f",
    fontSize: "0.85rem",
    marginTop: "4px",
    display: "block",
  },
  submitButton: {
    padding: "12px",
    backgroundColor: "#007bff",
    color: "#fff",
    fontWeight: "600",
    fontSize: "1.1rem",
    border: "none",
    borderRadius: "6px",
    cursor: "pointer",
    transition: "background-color 0.3s ease",
  },
  addBtn: {
    marginTop: "8px",
    backgroundColor: "#28a745",
    color: "white",
    border: "none",
    padding: "6px 12px",
    borderRadius: "4px",
    cursor: "pointer",
    fontSize: "0.9rem",
  },
  removeBtn: {
    marginLeft: "8px",
    backgroundColor: "#dc3545",
    color: "white",
    border: "none",
    padding: "0 10px",
    borderRadius: "4px",
    cursor: "pointer",
    fontSize: "1.3rem",
    lineHeight: "1",
    fontWeight: "bold",
  },
};

export default EditPrescription;
