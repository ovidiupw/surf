const initialState = {
  showAuthSpinner: false
};

export function spinner(state = initialState, action) {
  switch (action.type) {
    case 'SHOW_AUTH_SPINNER': {
      return Object.assign({}, state, {
        showAuthSpinner: true
      });
    }

    case 'HIDE_AUTH_SPINNER': {
      return Object.assign({}, state, {
        showAuthSpinner: false
      });
    }

    default: {
      return state;
    }
  };
}
