const errorModalInitialState = {
  visible: false,
  header: undefined,
  body: undefined,
  footer: undefined
};
const s3LinkModalInitialState = {
  visible: false,
  header: 'Crawled data from S3',
  cssLink: undefined,
  textLink: undefined
};

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

export function s3LinkModal(state = s3LinkModalInitialState, action) {
  switch (action.type) {
    case 'SHOW_S3_LINK_MODAL': {
      return Object.assign({}, state, {
        visible: true
      });
    }

    case 'HIDE_S3_LINK_MODAL': {
      return Object.assign({}, state, {
        visible: false
      });
    }

    case 'UPDATE_S3_LINK_MODAL_LINKS': {
      return Object.assign({}, state, {
        cssLink: action.cssLink,
        textLink: action.textLink
      });
    }

    default: {
      return state;
    }
  };
}
