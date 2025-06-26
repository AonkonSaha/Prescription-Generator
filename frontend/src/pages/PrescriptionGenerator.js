import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../navbar-footer/Navbar";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function PrescriptionGenerator() {
  const todayString = new Date().toISOString().split("T")[0];
  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";
  const token = localStorage.getItem("token");

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
  const navigate = useNavigate();

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
      const prescDate = new Date(form.prescriptionDate);
      if (nextDate <= prescDate) {
        newErrors.nextVisitDate = "Next Visit Date must be after Prescription Date";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) {
      toast.error("Please correct the errors in the form");
      return;
    }

    try {
      const response = await fetch(`${baseURL}/api/prescription/v1/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        const errorData = await response.json();
        toast.error("Failed to create prescription: " + (errorData.message || response.statusText));
        return;
      }

      toast.success("✅ Prescription created successfully!");

      setForm({
        prescriptionDate: todayString,
        patientName: "",
        patientAge: "",
        patientGender: "",
        diagnosis: [""],
        medicines: [""],
        nextVisitDate: "",
      });
      setErrors({});
    } catch (error) {
      toast.error("❌ Network error: " + error.message);
    }
  };

  return (
    <>
      <Navbar />
      <ToastContainer position="top-right" autoClose={3000} />
      <div style={styles.container}>
        <h2>Create Prescription</h2>
        <form onSubmit={handleSubmit} style={styles.form}>
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

          <div style={styles.formGroup}>
            <label>Diagnosis *</label>
            {form.diagnosis.map((diag, idx) => (
              <div key={idx} style={styles.dynamicRow}>
                <input
                  type="text"
                  placeholder={`Diagnosis #${idx + 1}`}
                  value={diag}
                  onChange={(e) => handleDiagnosisChange(idx, e.target.value)}
                  style={{ ...styles.input, flexGrow: 1 }}
                />
                {form.diagnosis.length > 1 && (
                  <button
                    type="button"
                    onClick={() => removeDiagnosis(idx)}
                    style={styles.removeButton}
                  >
                    &times;
                  </button>
                )}
              </div>
            ))}
            {errors.diagnosis && <span style={styles.error}>{errors.diagnosis}</span>}
            {form.diagnosis.map((_, idx) =>
              errors[`diagnosis_${idx}`] ? (
                <span key={idx} style={styles.error}>{errors[`diagnosis_${idx}`]}</span>
              ) : null
            )}
            <button type="button" onClick={addDiagnosis} style={styles.addButton}>
              + Add Diagnosis
            </button>
          </div>

          <div style={styles.formGroup}>
            <label>Medicines *</label>
            {form.medicines.map((med, idx) => (
              <div key={idx} style={styles.dynamicRow}>
                <input
                  type="text"
                  placeholder={`Medicine #${idx + 1}`}
                  value={med}
                  onChange={(e) => handleMedicinesChange(idx, e.target.value)}
                  style={{ ...styles.input, flexGrow: 1 }}
                />
                {form.medicines.length > 1 && (
                  <button
                    type="button"
                    onClick={() => removeMedicine(idx)}
                    style={styles.removeButton}
                  >
                    &times;
                  </button>
                )}
              </div>
            ))}
            {errors.medicines && <span style={styles.error}>{errors.medicines}</span>}
            {form.medicines.map((_, idx) =>
              errors[`medicines_${idx}`] ? (
                <span key={idx} style={styles.error}>{errors[`medicines_${idx}`]}</span>
              ) : null
            )}
            <button type="button" onClick={addMedicine} style={styles.addButton}>
              + Add Medicine
            </button>
          </div>

          <div style={styles.formGroup}>
            <label>Next Visit Date *</label>
            <input
              type="date"
              name="nextVisitDate"
              value={form.nextVisitDate}
              onChange={handleChange}
              style={styles.input}
            />
            {errors.nextVisitDate && <span style={styles.error}>{errors.nextVisitDate}</span>}
          </div>

          <button type="submit" style={styles.submitButton}>
            Create Prescription
          </button>
        </form>
      </div>
    </>
  );
}

const styles = {
  container: {
    marginTop: "80px",
    maxWidth: "700px",
    marginLeft: "auto",
    marginRight: "auto",
    padding: "20px",
    backgroundColor: "#f9f9f9",
    borderRadius: "8px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
  },
  form: {
    display: "flex",
    flexDirection: "column",
  },
  formGroup: {
    marginBottom: "15px",
    display: "flex",
    flexDirection: "column",
  },
  input: {
    padding: "8px",
    fontSize: "16px",
    borderRadius: "4px",
    border: "1px solid #ccc",
  },
  submitButton: {
    backgroundColor: "#2c3e50",
    color: "#fff",
    border: "none",
    padding: "10px",
    fontSize: "16px",
    borderRadius: "4px",
    cursor: "pointer",
    marginTop: "10px",
  },
  error: {
    color: "red",
    fontSize: "12px",
    marginTop: "4px",
  },
  dynamicRow: {
    display: "flex",
    alignItems: "center",
    marginBottom: "8px",
    gap: "8px",
  },
  removeButton: {
    backgroundColor: "red",
    border: "none",
    color: "#fff",
    fontSize: "20px",
    lineHeight: "1",
    padding: "0 8px",
    borderRadius: "4px",
    cursor: "pointer",
  },
  addButton: {
    backgroundColor: "#2c3e50",
    color: "#fff",
    border: "none",
    padding: "6px 12px",
    fontSize: "14px",
    borderRadius: "4px",
    cursor: "pointer",
    alignSelf: "flex-start",
  },
};

export default PrescriptionGenerator;
