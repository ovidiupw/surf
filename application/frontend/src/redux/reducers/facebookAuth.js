import {handleActions} from 'redux-actions';

export const initialState = {
  name: 'world'
};

export const authenticateWithFacebook = handleActions({
  HANDLE_AUTH_VIA_FACEBOOK: (state, action) => {
    console.log("Authenticating via Facebook...");
    return state;
  }
}, initialState);
