import AuthConstants from "./actionTypes";

export const authInProgress = (user) => ({
  type: AuthConstants.AUTH_IN_PROGRESS,
  payload: user,
});

export const authSuccess = (accessToken) => ({
  type: AuthConstants.AUTH_SUCCESS,
  payload: accessToken
});

export const authFailure = (error) => ({
  type: AuthConstants.AUTH_FAILED,
  payload: error.message,
});

export const logoutFailure = (error) => ({
  type: AuthConstants.AUTH_LOG_OUT_FAILED,
  payload: error.message,
});

export const logoutSuccess = () => ({
  type: AuthConstants.AUTH_LOGGED_OUT
});