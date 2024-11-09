import { useContext } from 'react';
import { Button } from 'react-bootstrap';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import { TokenContext } from '../../data/Token/TokenContext';

export default function FSNavbar() {
  const {token, setToken} = useContext(TokenContext);

  return (
    <Navbar expand="lg" className="bg-body-tertiary" data-bs-theme="dark">
      <Container>
        <Navbar.Brand href="/dashboard">Friendly Stats</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/dashboard">Dashboard</Nav.Link>
          </Nav>
          <Nav className="ml-auto">
            <Navbar.Text>
              USERNAME
            </Navbar.Text>
          <Button variant="outline-danger" className="mx-2" onClick={() => setToken(null)}>Logout</Button>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}