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
