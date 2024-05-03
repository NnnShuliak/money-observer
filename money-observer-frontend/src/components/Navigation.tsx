import { useState, useEffect } from "react";

import { Navbar, Container } from "react-bootstrap";
import { getUsername } from "../utils";
import "../styles/Navidation.css";

const Navigation: React.FC = () => {
  const [username, setUsername] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      getUsername(token)
        .then((username) => setUsername(username))
        .catch((error) => console.error("Error fetching username:", error));
    }
  }, []);

  const onLogout = () => {
    localStorage.removeItem("token");
  };

  return (
    <Navbar className="navigation bg-body-tertiary">
      <Container>
        <Navbar.Brand href="/home">Money observer</Navbar.Brand>
        <Navbar.Toggle />
        <Navbar.Collapse className="justify-content-end">
          <Navbar.Text className="navigation-text">
            Signed in as:{" "}
            <a href="/login" onClick={onLogout}>
              {username}
            </a>
          </Navbar.Text>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Navigation;
