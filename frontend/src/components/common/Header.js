import { Col, Container, Row, Navbar, Nav, NavbarBrand, NavItem, NavLink } from "reactstrap";
import { GoogleLogin, GoogleLogout } from "react-google-login";
import { authSuccess, authInProgress, authFailure, logoutSuccess } from "../../redux/auth/actions";
import AuthenticatedDropdown from "./AuthenticatedDropdown";
import { connect } from "react-redux";
import ky from 'ky';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCampground } from '@fortawesome/free-solid-svg-icons'

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
    <header style={{ display: 'flex', marginTop: 10 }}>
      <Container fluid style={{ backgroundColor: '#000', width: '30%' }}>
        <Container>
          <Navbar
            dark
            style={{ backgroundColor: '#000' }}>
            <Nav>
            <NavbarBrand href="/" ><FontAwesomeIcon icon={faCampground} size='2x' /></NavbarBrand>
              <NavItem className="m-auto">
                <NavLink style={{ color: 'white' }} href="/">bandcamper</NavLink>
              </NavItem>
            </Nav>
          </Navbar>
        </Container>
      </Container>
      <Container fluid style={{ backgroundColor: '#000', width: '70%' }}>
        <Container>

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