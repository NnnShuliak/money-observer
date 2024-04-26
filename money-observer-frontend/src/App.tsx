import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./App.css";
import RegistrationPage from "./components/Registration";
import Login from "./components/Login";
import HomePage from "./components/HomePage";
import NotFoundPage from "./components/NotFoundPage";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/registration" element={<RegistrationPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/home" element={<ProtectedRoute Component={HomePage} />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </Router>
  );
}

export default App;
