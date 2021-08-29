import { Col, Container, Row, Navbar, Nav, NavbarBrand, NavItem, NavLink } from "reactstrap";
import { GoogleLogin, GoogleLogout } from "react-google-login";
import { authSuccess, authInProgress, authFailure, logoutSuccess } from "../../redux/auth/actions";
import AuthenticatedDropdown from "./AuthenticatedDropdown";
import { connect } from "react-redux";
import ky from 'ky';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCaravan } from '@fortawesome/free-solid-svg-icons'

function Header(props) {

  const loginSuccess = response => {
    props.authInProgress(response.profileObj);
    getAppToken(response.accessToken);
  }

  const loginFailure = response => {
    props.authFailure(response);
  }

  const getAppToken = async (googleAccessToken) => {
    const apiUrl = process.env.REACT_APP_BANDCAMPER_API_URL
    const apiResponse = await ky.post(`${apiUrl}/authenticate`, {
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ "accessToken": googleAccessToken })
    }).json();
    props.authSuccess(apiResponse.accessToken);
  }
  const isAuthenticated = props.isAuthenticated;
  return (
    <header>
      <Container fluid className="bg-dark">
        <Container>
          <Row>
            <Col sm="9" md="9" lg="9">
              <Navbar
                dark
                className="bg-dark">
                <Nav>
                  <NavbarBrand href="/" ><FontAwesomeIcon icon={faCaravan} size='2x' /></NavbarBrand>
                  <NavItem className="m-auto">
                    <NavLink style={{ color: 'white' }} href="/">bandcamper</NavLink>
                  </NavItem>
                  <NavItem className="m-auto">
                    <NavLink style={{ color: 'white', marginLeft: 20, fontSize: 16 }} href="/create">create</NavLink>
                  </NavItem>
                </Nav>
              </Navbar>
            </Col>
            <Col sm="3" md="3" lg="3" className="m-auto">
              <Navbar style={{ justifyContent: 'flex-end' }}>
                <Nav>
                  <NavItem>
                    {isAuthenticated ?
                      <AuthenticatedDropdown /> : <GoogleLogin
                        clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}
                        buttonText="Sign in with Google"
                        onSuccess={loginSuccess}
                        onFailure={loginFailure} />
                    }
                  </NavItem>
                </Nav>
              </Navbar>
            </Col>
          </Row>
        </Container>
      </Container>
    </header>
  );
}

const mapStateToProps = (state) => {
  return { isAuthenticated: state.auth.isAuthenticated };
};

const mapDispatchToProps = (dispatch) => ({
  authInProgress: (response) => {
    dispatch(authInProgress(response));
  },
  authSuccess: (accessToken) => {
    dispatch(authSuccess(accessToken));
  },
  logoutSuccess: () => {
    dispatch(logoutSuccess());
  },
  authFailure: (error) => {
    dispatch(authFailure(error));
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(Header);