import React, { useState } from "react";
import { Button, Form, Modal } from "react-bootstrap";
import { getCategories } from "../utils";

interface Props {
  show: boolean;
  handleClose?: () => void;
  setCategories: React.Dispatch<React.SetStateAction<any[]>>;
  categories: any[];
}

const CategoryCreationForm: React.FC<Props> = ({
  show,
  handleClose,
  setCategories,
}) => {
  const [formData, setFormData] = useState({
    title: "",
    ratio: 0,
  });

  const token = localStorage.getItem("token");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    console.log(JSON.stringify(formData));
    try {
      formData.ratio /= 100;
      const res = await fetch("http://localhost:8080/api/categories", {
        method: "POST",
        body: JSON.stringify(formData),
        headers: {
          "Content-type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      formData.ratio *= 100;
      if (res.ok) {
        const data = await res.json();
        console.log("Category created:", data);
        handleClose && handleClose();

        setCategories(await (await getCategories(token)).json());
      } else {
        throw new Error("Creation category failed");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Creation category failed");
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
        <Modal.Title>New category</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group controlId="title">
            <Form.Label>Title</Form.Label>
            <Form.Control
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group controlId="ratio">
            <Form.Label>Max available percent</Form.Label>
            <Form.Control
              type="number"
              name="ratio"
              value={formData.ratio}
              onChange={handleChange}
              required
              min={1}
              step={1}
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

export default CategoryCreationForm;
