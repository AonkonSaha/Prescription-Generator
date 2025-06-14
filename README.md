# 📝 Prescription Generator

A **full-stack web application** to help doctors generate, manage, and review prescriptions with ease.  
Built using **React.js** for the frontend and **Spring Boot** for the backend, with an in-memory **H2 database** for seamless local development.

---

## 🚀 Features

- 🔐 Doctor registration and login (JWT-based authentication & authorization)
- 💊 Create and manage prescriptions
- 📄 Generate & download PDF prescriptions
- 📈 View reports for better tracking
- 📱 Responsive, clean, and modern UI/UX

---

## 🛠 Tech Stack

- **Frontend:** React.js  
- **Backend:** Spring Boot (Java)  
- **Database:** H2 (in-memory for development)  
- **Authentication:** JWT (JSON Web Tokens)
- **PDF Generation:** OpenPDF and ZXing (QR Code)

---

## ⚙️ Prerequisites

Ensure the following tools are installed:

- ✅ **Java 21+** (for Spring Boot)
- ✅ **Gradle-Groovy** (to build the backend)
- ✅ **Node.js v22.16.0+** and **npm** (to run the frontend)

---

## 🧩 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/prescription-generator.git
cd prescription-generator
```

### 2. Frontend Setup

```bash
cd frontend
npm install
npm start
```

### 3. Backend Setup

```bash
cd backend
./gradlew bootRun or Run from IntelliJ Idea(Optional)
## Alternatively, you can run the application directly from IntelliJ IDEA (optional)
```

## 📅 Prescription Date Logic

By default, doctors can select **any date** when creating a prescription.

To **restrict** prescriptions to **today’s date only**, enable date validation:

### ✅ Frontend (React)

Edit `PrescriptionGenerator.js` and `EditPrescription.js` to uncomment the following:

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

### ✅ Backend (Spring Boot)

In `PrescriptionValidationImpl.java`, ensure the date check is enabled:

```java
if (!prescriptionValidationService.isValidPrescriptionDate(prescriptionDTO.getPrescriptionDate())) {
    return "Prescription Date must be in Today!";
}
```

---
## 🔍 Feature Details
**Prescription Management**
- Create, update, and delete prescriptions with all necessary fields validated 

**Prescription List Page**
- Initially fetches prescriptions for the current month in ascending order by date
- Allows filtering by start and end dates, automatically updating the list on filter changes
- Clear filter resets to show the current month's prescriptions

**PDF Prescription Download**
- Generates professional layout with patient/doctor info and medicines
- Includes a QR code linking back to prescription details
- Fully styled and print-ready

**Report Page**
- Displays prescription counts per day in ascending order for easy tracking

**COVID-19 Statistics API**
- Consumes a third-party backend API to fetch and display COVID-19 stats dynamically

---

## 🙌 Final Notes

Once both frontend and backend are running:
- Register a User
- Log in using the user credentials
- Create a prescription
- Explore prescriptions, reports and features

---

## Important
- Since the backend uses an in-memory H2 database, all data is lost when the backend server stops. If you restart the backend without logging out from the frontend, your stored JWT token will become invalid. In this case, please clear the token from your browser’s local storage manually.
- If you attempt to logout with an invalid token (because the user no longer exists in the backend), the backend will safely ignore the request. This behavior prevents errors but requires you to clear tokens to avoid inconsistencies.
---

Enjoy coding and enhancing the Prescription Generator! 🎉