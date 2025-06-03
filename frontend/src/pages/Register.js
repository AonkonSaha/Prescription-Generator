import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    name: "",
    mobileNumber: "",
    email: "",
    gender: "",
    designation: "",
    licenseNumber: "",
    degrees: [""],
    dateOfBirth: "",
    password: "",
    confirmPassword: "",
  });

  const [fieldErrors, setFieldErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const baseURL = process.env.REACT_APP_BACK_END_BASE_URL || "http://localhost:8080";

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleDegreeChange = (index, value) => {
    const newDegrees = [...form.degrees];
    newDegrees[index] = value;
    setForm((prev) => ({ ...prev, degrees: newDegrees }));
  };

  const addDegree = () => {
    setForm((prev) => ({ ...prev, degrees: [...prev.degrees, ""] }));
  };

  const removeDegree = (index) => {
    if (form.degrees.length === 1) return;
    const newDegrees = form.degrees.filter((_, i) => i !== index);
    setForm((prev) => ({ ...prev, degrees: newDegrees }));
  };

  const validateEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  const validateMobile = (mobile) => /^[0-9]{10,15}$/.test(mobile);

  const validateForm = () => {
    const errors = {};
    if (!form.name.trim()) errors.name = "Full Name is required.";
    if (!form.mobileNumber.trim()) errors.mobileNumber = "Mobile Number is required.";
    else if (!validateMobile(form.mobileNumber)) errors.mobileNumber = "Invalid Mobile Number.";

    if (!form.email.trim()) errors.email = "Email is required.";
    else if (!validateEmail(form.email)) errors.email = "Invalid Email.";

    if (!form.gender) errors.gender = "Gender is required.";
    if (!form.designation.trim()) errors.designation = "Designation is required.";
    if (!form.licenseNumber.trim()) errors.licenseNumber = "License Number is required.";

    if (!form.degrees.length || form.degrees.every((d) => !d.trim()))
      errors.degrees = "At least one degree is required.";
    else {
      form.degrees.forEach((deg, idx) => {
        if (!deg.trim()) errors[`degrees_${idx}`] = "Degree cannot be empty.";
      });
    }

    if (!form.dateOfBirth) errors.dateOfBirth = "Date of Birth is required.";
    if (!form.password) errors.password = "Password is required.";
    else if (form.password.length < 8) errors.password = "Minimum 8 characters.";

    if (!form.confirmPassword) errors.confirmPassword = "Confirm Password is required.";
    else if (form.password !== form.confirmPassword)
      errors.confirmPassword = "Passwords do not match.";

    setFieldErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleRegister = async () => {
    if (!validateForm()) {
      toast.error("Please enter valid details in the form!");
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`${baseURL}/api/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          ...form,
          degrees: form.degrees.filter((d) => d.trim() !== ""),
        }),
      });

      const data = await response.text();
      if (response.ok) {
        toast.success("Registration successful! Redirecting...");
        setTimeout(() => navigate("/login"), 2000);
      } else {
        toast.error(data || "Registration failed.");
      }
    } catch (err) {
      toast.error(err.message || "Server error. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar closeOnClick pauseOnHover />
      <div style={styles.container}>
        <div style={styles.card} role="main" aria-label="Doctor registration form">
          <h2 style={styles.title}>Doctor Registration</h2>

          <Input label="Full Name" name="name" value={form.name} onChange={handleChange} error={fieldErrors.name} />
          <Input label="Mobile Number" name="mobileNumber" value={form.mobileNumber} onChange={handleChange} error={fieldErrors.mobileNumber} />
          <Input label="Email" name="email" value={form.email} onChange={handleChange} error={fieldErrors.email} />

          <label htmlFor="gender" style={styles.label}>Gender *</label>
          <select id="gender" name="gender" value={form.gender} onChange={handleChange} style={styles.input} disabled={loading}>
            <option value="">-- Select Gender --</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Others">Others</option>
          </select>
          {fieldErrors.gender && <p style={styles.error}>{fieldErrors.gender}</p>}

          <Input label="Designation" name="designation" value={form.designation} onChange={handleChange} error={fieldErrors.designation} />
          <Input label="License Number" name="licenseNumber" value={form.licenseNumber} onChange={handleChange} error={fieldErrors.licenseNumber} />

          <label style={styles.label}>Degrees *</label>
          {form.degrees.map((degree, idx) => (
            <div key={idx} style={styles.degreeRow}>
              <input
                type="text"
                placeholder={`Degree #${idx + 1}`}
                value={degree}
                onChange={(e) => handleDegreeChange(idx, e.target.value)}
                style={styles.input}
                disabled={loading}
              />
              {form.degrees.length > 1 && (
                <button
                  type="button"
                  onClick={() => removeDegree(idx)}
                  style={styles.removeButton}
                  disabled={loading}
                >
                  &times;
                </button>
              )}
            </div>
          ))}
          {fieldErrors.degrees && <p style={styles.error}>{fieldErrors.degrees}</p>}
          {form.degrees.map((_, idx) =>
            fieldErrors[`degrees_${idx}`] ? (
              <p key={idx} style={styles.error}>{fieldErrors[`degrees_${idx}`]}</p>
            ) : null
          )}

          <button onClick={addDegree} style={styles.addButton} type="button" disabled={loading}>
            + Add Another Degree
          </button>

          <Input label="Date of Birth" name="dateOfBirth" type="date" value={form.dateOfBirth} onChange={handleChange} error={fieldErrors.dateOfBirth} />
          <Input label="Password" name="password" type="password" value={form.password} onChange={handleChange} error={fieldErrors.password} />
          <Input label="Confirm Password" name="confirmPassword" type="password" value={form.confirmPassword} onChange={handleChange} error={fieldErrors.confirmPassword} />

          <button
            onClick={handleRegister}
            style={{ ...styles.button, ...(loading ? styles.buttonDisabled : {}) }}
            disabled={loading}
            type="button"
          >
            {loading ? "Registering..." : "Register"}
          </button>

          <p style={styles.registerText}>
            Already have an account?{" "}
            <span style={styles.registerLink} onClick={() => navigate("/login")}>
              Login here
            </span>
          </p>
        </div>
      </div>
    </>
  );
}

const Input = ({ label, name, value, onChange, error, type = "text" }) => (
  <>
    <label htmlFor={name} style={styles.label}>{label} *</label>
    <input
      id={name}
      name={name}
      type={type}
      value={value}
      onChange={onChange}
      style={styles.input}
    />
    {error && <p style={styles.error}>{error}</p>}
  </>
);

const styles = {
  container: {
    height: "100vh",
    background: "linear-gradient(135deg,rgb(249, 250, 252) 0%,rgb(247, 246, 248) 100%)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
    padding: "20px",
  },
  card: {
    backgroundColor: "white",
    padding: "40px 50px",
    borderRadius: "12px",
    boxShadow: "0 8px 20px rgba(0,0,0,0.15)",
    width: "480px",
    maxWidth: "100%",
    textAlign: "left",
    overflowY: "auto",
    maxHeight: "90vh",
  },
  title: {
    marginBottom: "24px",
    color: "#333",
    textAlign: "center",
    fontWeight: "700",
    fontSize: "24px",
  },
  label: {
    display: "block",
    marginBottom: "6px",
    fontWeight: "600",
    color: "#555",
  },
  input: {
    width: "100%",
    padding: "12px 15px",
    marginBottom: "20px",
    borderRadius: "8px",
    border: "1px solid #ccc",
    fontSize: "16px",
    outline: "none",
    transition: "border-color 0.3s",
    boxSizing: "border-box",
  },
  error: {
    color: "#e74c3c",
    marginTop: "-16px",
    marginBottom: "12px",
    fontWeight: "600",
    fontSize: "14px",
  },
  button: {
    width: "100%",
    padding: "12px 15px",
    backgroundColor: "#667eea",
    color: "white",
    fontWeight: "600",
    fontSize: "16px",
    borderRadius: "8px",
    border: "none",
    cursor: "pointer",
    transition: "background-color 0.3s",
  },
  buttonDisabled: {
    backgroundColor: "#a2a6f4",
    cursor: "not-allowed",
  },
  addButton: {
    padding: "8px 12px",
    marginBottom: "20px",
    backgroundColor: "#eee",
    border: "1px solid #bbb",
    borderRadius: "6px",
    cursor: "pointer",
    fontWeight: "500",
  },
  degreeRow: {
    display: "flex",
    alignItems: "center",
    gap: "8px",
  },
  removeButton: {
    backgroundColor: "#e74c3c",
    color: "#fff",
    border: "none",
    borderRadius: "50%",
    width: "26px",
    height: "26px",
    cursor: "pointer",
    fontWeight: "700",
    fontSize: "18px",
    lineHeight: "22px",
    textAlign: "center",
    padding: "0",
  },
  registerText: {
    marginTop: "24px",
    textAlign: "center",
    fontSize: "15px",
    color: "#666",
  },
  registerLink: {
    color: "#667eea",
    cursor: "pointer",
    fontWeight: "600",
  },
};

export default Register;
