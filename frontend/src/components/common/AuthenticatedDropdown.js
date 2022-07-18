import React, { useState } from 'react';
import { Dropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';
import { connect } from "react-redux";
import { useGoogleLogout } from 'react-google-login';
import { logoutFailure, logoutSuccess } from '../../redux/auth/actions';
import { useHistory } from 'react-router-dom';

function AuthenticatedDropdown(props) {

  const [dropdownOpen, setDropdownOpen] = useState(false);

  const toggle = () => setDropdownOpen(prevState => !prevState);

  const history = useHistory();

  const logoutSuccess = () => {
    props.logoutSuccess();
  }

  const logoutFailure = () => {
    props.logoutFailure();
  }

  const { signout } = useGoogleLogout({
    clientId: process.env.REACT_APP_GOOGLE_CLIENT_ID,
    onLogoutSuccess: logoutSuccess,
    onFailure: logoutFailure
  });

  return (
    <Dropdown isOpen={dropdownOpen} toggle={toggle}>
      <DropdownToggle caret className="bg-white " style={{ width: 'fit-content', height: 50, color: 'black', fontSize: 15 }}>
        {props.user.name}
      </DropdownToggle>
      <DropdownMenu style={{ borderRadius: 6 }} >
        <DropdownItem header><strong>{props.user.email}</strong></DropdownItem>
        <DropdownItem>{props.user.name}</DropdownItem>
        <DropdownItem>Account</DropdownItem>
        <DropdownItem>Notifications</DropdownItem>
        <DropdownItem divider />
        <DropdownItem onClick={logoutSuccess}>Logout</DropdownItem>
      </DropdownMenu>
    </Dropdown>
  );
}

const mapStateToProps = (state) => {
  return { user: state.auth.user };
};

const mapDispatchToProps = (dispatch) => ({
  logoutSuccess: () => {
    dispatch(logoutSuccess());
  },
  logoutFailure: () => {
    dispatch(logoutFailure());
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(AuthenticatedDropdown);