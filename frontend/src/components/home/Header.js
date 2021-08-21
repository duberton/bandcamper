import react from "react";
import { Col, Container, Row, Navbar, NavbarBrand } from "reactstrap";
import { GoogleLogin } from "react-google-login";

function Header() {

  function loginSuccess(response) {
    console.log('login');
  }

  function loginFailure(response) {
    console.log(response);
  }

  return (
    <header>
      <Container fluid className="bg-dark">
        <Container>
          <Row>
            <Col sm="9" md="9" lg="9">
              <Navbar
                dark
                className="bg-dark">
                <NavbarBrand href="/">bandcamper</NavbarBrand>
              </Navbar>
            </Col>
            <Col sm="3" md="3" lg="3">
              <Navbar>
                <GoogleLogin
                  clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}
                  buttonText="Sign in with Google"
                  onSuccess={loginSuccess()}
                  onFailure={loginFailure()}
                />
              </Navbar>
            </Col>
          </Row>
        </Container>
      </Container>
    </header>
  );
}

export default Header;