import './App.css';
import {Router, Routes, Route} from 'react-router-dom';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login'
import Home from './pages/Home';
import PrescriptionList from './pages/PrescriptionList';
import PrescriptionGenerator from './pages/PrescriptionGenerator';
import Reports from './pages/Reports';
import Profile from './pages/Profile';
import { Navigate } from 'react-router-dom';
import Register from './pages/Register';
import EditPrescription from './pages/EditPrescription';
function App() {
  return (
      <Routes>
        <Route path="/login" element={<Login/>} />
        <Route path="/register" element={<Register/>} />
        <Route path="/home" element={<ProtectedRoute><Home /></ProtectedRoute>} />
        <Route path="/prescriptions" element={<ProtectedRoute><PrescriptionList /></ProtectedRoute>} />
        <Route path="/prescription/generate" element={<ProtectedRoute><PrescriptionGenerator/></ProtectedRoute>} />
        <Route path="/reports" element={<ProtectedRoute><Reports /></ProtectedRoute>} />
        <Route path="/prescription/edit/:id" element={<EditPrescription />} />
        <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
  );
}

export default App;
