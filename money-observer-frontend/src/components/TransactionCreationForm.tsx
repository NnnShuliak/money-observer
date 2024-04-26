import React, { useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { getCategories, getExpenses, getIncome, getTotalncome } from "../utils";

interface Props {
  show: boolean;
  handleClose?: () => void;
  setTransactions: React.Dispatch<React.SetStateAction<any[]>>;
  categoryId?: number;
  setCategories: React.Dispatch<React.SetStateAction<any[]>>;
  setTotalIncome: React.Dispatch<React.SetStateAction<number>>;
}

const ExpenseCreationForm: React.FC<Props> = ({
  show,
  handleClose,
  setTransactions,
  categoryId,
  setCategories,
  setTotalIncome,
}) => {
  const [formData, setFormData] = useState({
    description: "",
    time: "",
    amount: 0,
  });

  const token = localStorage.getItem("token");

  const handleSubmit = async (e: React.FormEvent) => {
    let requestUrl = "http://localhost:8080/api/";
    requestUrl +=
      categoryId !== undefined ? `categories/${categoryId}/expenses` : "income";
    e.preventDefault();
    console.log(requestUrl);
    console.log(JSON.stringify(formData));
    try {
      const res = await fetch(requestUrl, {
        method: "POST",
        body: JSON.stringify(formData),
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.ok) {
        const data = await res.json();
        console.log("Transaction created:", data);
        handleClose && handleClose();

        let response: Response;

        if (categoryId !== undefined) {
          response = await getExpenses(token, categoryId);
        } else {
          response = await getIncome(token);
          setTotalIncome(await getTotalncome(token));
        }
        response.ok && setTransactions(await response.json());

        const categoriesResponse = await getCategories(token);
        categoriesResponse.ok && setCategories(await categoriesResponse.json());
      } else {
        throw new Error("Creation transaction failed");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Creation transaction failed");
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>New {categoryId ? "expense" : "income"}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="description">
            <Form.Label>Description</Form.Label>
            <Form.Control
              type="text"
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group controlId="time">
            <Form.Label>Time</Form.Label>
            <Form.Control
              type="datetime-local"
              name="time"
              value={formData.time}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group controlId="amount">
            <Form.Label>Amount</Form.Label>
            <Form.Control
              type="number"
              name="amount"
              value={formData.amount}
              onChange={handleChange}
              required
              min={0}
              step={0.01}
            />
          </Form.Group>
          <div className="d-flex justify-content-end">
            <Button variant="primary" type="submit">
              Add
            </Button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default ExpenseCreationForm;
