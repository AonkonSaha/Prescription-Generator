
# 🩺 Prescription Generator – Full Stack Web App

A complete and scalable **prescription management system** designed for doctors and admins to securely **create**, **manage**, and **download medical prescriptions**.  
Built with **React.js** and **Spring Boot**, it now supports **multi-role authentication**, **MySQL persistence**, and **well-documented REST APIs** with Swagger.

---

## 🚀 Key Features

- 🔐 **JWT-based Authentication** – Secure login for Doctors and Admins  
- 👨‍⚕️ **Doctor Dashboard** – Create, edit, and view prescriptions  
- 🧑‍💼 **Admin Panel** – Manage users, roles, and system data  
- 📄 **PDF Generator** – Professional prescription layout with QR code  
- 📊 **Reports Page** – Daily prescription statistics  
- 🌐 **Live COVID-19 Stats** – Dynamic data from external API  
- 🧭 **Swagger/OpenAPI Docs** – Fully documented REST APIs for easy integration  
- 🧑‍🤝‍🧑 **Multi-Role Support** – Role-based route access and control  
- 💾 **MySQL Integration** – Real data persistence  

---

## 🧰 Tech Stack

| Layer        | Technology                             |
|--------------|----------------------------------------|
| Frontend     | React.js (Inline CSS)                  |
| Backend      | Spring Boot (Java 21+)                 |
| Database     | MySQL                                  |
| Auth         | JWT (JSON Web Tokens)                  |
| PDF Support  | OpenPDF + ZXing (for QR Code)          |
| Docs         | Swagger UI (SpringDoc OpenAPI)         |
| Build Tools  | Gradle (Groovy DSL), Node.js + npm     |
| Architecture | Domain-Driven Design, SOLID , DRY      |

---

## ⚙️ Prerequisites

Ensure you have the following installed:

- ✅ **Java 21+**
- ✅ **Gradle (Groovy DSL)**
- ✅ **Node.js v22.16.0+**
- ✅ **npm**
- ✅ **MySQL Server running**

---

## 🔐 Default Dummy User (Preloaded)

To get started quickly, a dummy User account is inserted on application startup:

| Field         | Value             |
|---------------|-------------------|
| Name          | `Aonkon Saha`     |
| Mobile Number | `01881264859`     |
| Password      | `12345678`        |
| Role          | `DOCTOR` `ADMIN` |

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/prescription-generator.git
cd prescription-generator
```

### 2️⃣ Backend Setup

```bash
cd backend
# Set your MySQL credentials in `application.yml`
./gradlew bootRun
# or run via IntelliJ IDEA
```

### 3️⃣ Frontend Setup

```bash
cd frontend
npm install
npm run start
```

---

## 📘 Swagger API Documentation

Auto-generated documentation is available via:

\`\`\`
http://localhost:8080/swagger-ui.html
\`\`\`

All endpoints include:
- ✨ `@Operation` summaries
- 🧾 `@ApiResponse` and `@Schema`-based response structures
- 🔐 `@SecurityRequirement` for secured routes
- 🧩 `@Parameter` descriptions for `@PathVariable` inputs

---

## 👮 Role-Based Access Control

- `ROLE_DOCTOR`: Can manage prescriptions, view reports
- `ROLE_ADMIN`: Can view users, assign roles, manage system data

---

## 📅 Prescription Date Validation

By default, any date is allowed. To restrict to **today only**:

### ✅ Frontend Validation

Uncomment in `PrescriptionGenerator.js` & `EditPrescription.js`:

```js
if (!form.prescriptionDate) {
  newErrors.prescriptionDate = "Prescription Date is required";
} else {
  const prescDate = new Date(form.prescriptionDate);
  prescDate.setHours(0, 0, 0, 0);
  if (prescDate.getTime() !== today.getTime()) {
    newErrors.prescriptionDate = "Prescription Date must be today's date";
  }
}
```

### ✅ Backend Validation

Ensure this logic is active in `PrescriptionValidationImpl.java`:

```java
if (!prescriptionValidationService.isValidPrescriptionDate(prescriptionDTO.getPrescriptionDate())) {
    return "Prescription Date must be in Today!";
}
```

---

## 🔍 Feature Overview

### 📋 Prescription Management
- Create, edit, and delete prescriptions
- Auto-sorted list by date, with date-range filters
- Real-time form validation

### 🧾 PDF Generator
- Doctor & patient info
- QR code linking to prescription details
- Print-ready and professional design

### 📈 Reports Page
- Daily prescription count for analytics
- Filterable by date

### 🦠 COVID-19 Stats
- Displays live COVID data fetched from a third-party API

### 🧑‍💼 Admin Panel
- View all users
- Assign roles
- Manage doctors and system metadata

---

---

## ✅ Summary

- Login with Doctor/Admin
- Manage User Roles
- Manage secure JWT sessions
- Create/edit prescriptions
- Generate PDFs with QR code
- View analytics and system reports
- Use Swagger to explore and test APIs

---

## 🙌 Final Notes

**Happy coding with the Prescription Generator!** 💊📄
