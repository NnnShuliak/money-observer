import React, { useState } from "react";
import {Form, Button, Alert} from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const RegistrationPage: React.FC = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
  });
  const [showAlert, setShowAlert] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    const res = await fetch("http://localhost:8080/api/auth/signup", {
      method: "POST",
      body: JSON.stringify(formData),
      headers: {
        "Content-type": "application/json",
      },
    });
    if (res.ok) {
      alert("Verification was send on your email!")
      navigate("/login");
    }else{
      setShowAlert(true);
    }
    console.log("Registration data:", formData);
  }

  return (
    <div  className="container p-5 loginPage" style={{ width: "500px" }}>
      <h2>Registration</h2>
      {showAlert && (
          <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible>
            Registration failed. Please try again.
          </Alert>
      )}
      <Form onSubmit={handleSubmit}>
        <Form.Group controlId="formUsername">
          <Form.Label>Username</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter username"
            name="name"
            value={formData.name}
            onChange={handleChange}
          />
        </Form.Group>

        <Form.Group controlId="formEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control
            type="email"
            placeholder="Enter email"
            name="email"
            value={formData.email}
            onChange={handleChange}
          />
        </Form.Group>

        <Form.Group controlId="formPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            name="password"
            value={formData.password}
            onChange={handleChange}
          />
        </Form.Group>

        <Button className="mt-3" variant="primary" type="submit">
          Register
        </Button>
      </Form>
      <div className="mt-3 text-center">
        Already have an account?{" "}
        <a href="/login">Log in here</a>
      </div>
    </div>
  );
};

export default RegistrationPage;
