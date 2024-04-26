import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { Link } from "react-router-dom";

const UnauthenticatedPage: React.FC = () => {
  return (
    <Container className="mt-5">
      <Row className="justify-content-center text-center">
        <Col md={8}>
          <h1 className="display-4 mb-4">Welcome to Money Observer!</h1>
          <p className="lead">
            You're not logged in. Please log in or register to access the
            content.
          </p>
        </Col>
      </Row>
      <Row className="justify-content-center mt-4">
        <Col md={6} className="d-flex justify-content-center">
          <Link to="/login">
            <Button variant="primary" size="lg" className="m-3">
              Login
            </Button>
          </Link>
          <Link to="/registration">
            <Button variant="outline-primary" size="lg" className="m-3">
              Register
            </Button>
          </Link>
        </Col>
      </Row>
    </Container>
  );
};

export default UnauthenticatedPage;
