export function showErrorModal(header, body, footer) {
  return {
    type: 'SHOW_ERROR_MODAL',
    header,
    body,
    footer
  };
}

export function hideErrorModal() {
  return {
    type: 'HIDE_ERROR_MODAL'
  };
}

export function showS3LinkModal() {
  return {
    type: 'SHOW_S3_LINK_MODAL'
  };
}

export function hideS3LinkModal() {
  return {
    type: 'HIDE_S3_LINK_MODAL'
  };
}

export function updateS3LinkModalLinks(cssLink, textLink) {
  return {
    type: 'UPDATE_S3_LINK_MODAL_LINKS',
    cssLink,
    textLink
  };
}
