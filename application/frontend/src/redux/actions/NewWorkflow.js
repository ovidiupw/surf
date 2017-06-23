export function updateNewWorkflowDefinition(newWorkflowDefinition) {
  return {
    type: 'UPDATE_NEW_WORKFLOW_DEFINTION',
    newWorkflowDefinition
  };
}

export function updateFormErrorsToRetry(formErrors) {
  return {
    type: 'UPDATE_FORM_ERRORS_TO_RETRY',
    formErrors
  };
}

export function updateCurrentSelectedFormError(error) {
  return {
    type: 'UPDATE_CURRENT_SELECTED_FORM_ERROR',
    error
  };
}

export function showCrawlerRetriersInputForm() {
  return {
    type: 'SHOW_CRAWLER_RETRIERS_INPUT_FORM'
  };
}

export function hideCrawlerRetriersInputForm() {
  return {
    type: 'HIDE_CRAWLER_RETRIERS_INPUT_FORM'
  };
}

export function toggleRetryAllErrorsTrueRadioButton() {
  return {
    type: 'TOGGLE_RETRY_ALL_ERRORS_TRUE_RADIO_BUTTON'
  };
}

export function toggleRetryAllErrorsFalseRadioButton() {
  return {
    type: 'TOGGLE_RETRY_ALL_ERRORS_FALSE_RADIO_BUTTON'
  };
}

export function updateRetryFormRetryInterval(formRetryInterval) {
  return {
    type: 'UPDATE_RETRY_FORM_RETRY_INTERVAL',
    formRetryInterval
  };
}

export function updateRetryFormBackoffRate(formRetryBackoffRate) {
  return {
    type: 'UPDATE_RETRY_FORM_BACKOFF_RATE',
    formRetryBackoffRate
  };
}

export function updateRetryFormMaximumAttempts(formRetryMaximumAttempts) {
  return {
    type: 'UPDATE_RETRY_FORM_MAXIMUM_ATTEMPTS',
    formRetryMaximumAttempts
  };
}

export function showCrawlerTextSelectorsInputForm() {
  return {
    type: 'SHOW_CRAWLER_TEXT_SELECTORS_INPUT_FORM'
  };
}

export function hideCrawlerTextSelectorsInputForm() {
  return {
    type: 'HIDE_CRAWLER_TEXT_SELECTORS_INPUT_FORM'
  };
}

export function showCrawlerCSSSelectorsInputForm() {
  return {
    type: 'SHOW_CRAWLER_CSS_SELECTORS_INPUT_FORM'
  };
}

export function hideCrawlerCSSSelectorsInputForm() {
  return {
    type: 'HIDE_CRAWLER_CSS_SELECTORS_INPUT_FORM'
  };
}
