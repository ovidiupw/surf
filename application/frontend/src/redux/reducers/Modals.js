const errorModalInitialState = {
  visible: false,
  header: undefined,
  body: undefined,
  footer: undefined
}

export function errorModal(state = errorModalInitialState, action) {
  switch (action.type) {
    case 'SHOW_ERROR_MODAL': {
      return Object.assign({}, state, {
        visible: true,
        header: action.header,
        body: action.body,
        footer: action.footer
      });
    }

    case 'HIDE_ERROR_MODAL': {
      return Object.assign({}, state, {
        visible: false,
        header: undefined,
        body: undefined,
        footer: undefined
      });
    }

    default: {
      return state;
    }
  };
}
