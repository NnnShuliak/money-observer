import React from "react";
import { Container, Row, Col, Button } from "react-bootstrap";
import { Link } from "react-router-dom";

const NotFoundPage: React.FC = () => {
  return (
    <Container className="mt-5">
      <Row>
        <Col>
          <h1 className="text-center">404 - Page Not Found</h1>
          <p className="text-center">
            Oops! The page you're looking for doesn't exist.
          </p>
          <div className="text-center">
            <Link to="/home">
              <Button variant="primary">Go Back Home</Button>
            </Link>
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default NotFoundPage;
