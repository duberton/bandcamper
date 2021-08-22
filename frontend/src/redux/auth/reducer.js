import AuthConstants from './actionTypes';

export const initialState = {
  isAuthenticated: false,
  user: {},
  accessToken: ""
};

export const authReducer = (state = initialState, action) => {
  switch (action.type) {
    case AuthConstants.AUTH_SUCCESS:
      return {
        ...state,
        isAuthenticated: true,
        accessToken: action.payload
      };
    case AuthConstants.AUTH_FAILED:
      return {
        ...state,
        isAuthenticated: false,
        accessToken: ""
      };
    case AuthConstants.AUTH_IN_PROGRESS:
      return {
        ...state,
        isAuthenticated: false,
        accessToken: "",
        user: action.payload
      };
    case AuthConstants.AUTH_LOGGED_OUT:
      return {
        ...state,
        isAuthenticated: false,
        accessToken: ""
      };
    case AuthConstants.AUTH_LOG_OUT_FAILED:
      return {
        ...state,
        isAuthenticated: true
      };
    default:
      return state;
  }
};