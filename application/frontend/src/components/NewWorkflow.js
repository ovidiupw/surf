import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import ReactTooltip from 'react-tooltip';
import ElementalFormTextInput from 'components/ElementalFormTextInput';
import ExponentialBackoffRetrierCard from 'components/ExponentialBackoffRetrierCard';
import Utility from 'modules/Utility';
import CardWithDeleteButton from 'components/CardWithDeleteButton';
import Loader from 'react-loader-advanced';
import {Spinner} from 'elemental';
import {
  Button,
  Row,
  Col,
  Form,
  Card,
  FormField,
  Radio,
  FormSelect,
  InputGroup
} from 'elemental';

let Highlight = require('react-highlight');

class NewWorkflow extends React.Component {

  constructor() {
    super();
    this.formFieldStyle = {
      width: '100%',
      margin: 0
    };
  }

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount(this.props.apigClient);

    let that = this;
    setTimeout(function() {
      if (that.props.apigClient == null) {
        that.props.gotoHome();
      }
    }, 100);
  }

  handleRetryAllErrorsTrueRadioButtonClick() {
    if (this.props.retryAllErrorsFalseRadioButtonChecked) {
      this.props.toggleRetryAllErrorsFalseRadioButton();
    }
    if (!this.props.retryAllErrorsTrueRadioButtonChecked) {
      this.props.toggleRetryAllErrorsTrueRadioButton();
    }
  }

  handleRetryAllErrorsFalseRadioButtonClick() {
    if (this.props.retryAllErrorsTrueRadioButtonChecked) {
      this.props.toggleRetryAllErrorsTrueRadioButton();
    }
    if (!this.props.retryAllErrorsFalseRadioButtonChecked) {
      this.props.toggleRetryAllErrorsFalseRadioButton();
    }
  }

  handleInputChange(event, inputName) {
    let workflowDef = this.props.newWorkflowDefinition;

    switch (inputName) {
      case "retry-interval-seconds": {
        let retryIntervalSeconds = event.target.value;
        if (retryIntervalSeconds == null
          || retryIntervalSeconds.length == 0
          || isNaN(retryIntervalSeconds)
          || retryIntervalSeconds < 0) {
          this.props.showInputValidationError("Retry interval seconds must be a valid number >= 0!");
        }

        this.props.updateRetryFormRetryInterval(retryIntervalSeconds);
        this.forceUpdate();
        break;
      }
      case "retry-backoff-rate": {
        let retryBackoffRate = event.target.value;
        if (retryBackoffRate == null
          || retryBackoffRate.length == 0
          || isNaN(retryBackoffRate)
          || retryBackoffRate < 1) {
          this.props.showInputValidationError("Retry backoff rate must be a number >= 1!");
        }

        this.props.updateRetryFormBackoffRate(retryBackoffRate);
        this.forceUpdate();
        break;
      }
      case "retry-max-attempts": {
        let retryMaxAttempts = event.target.value;
        if (retryMaxAttempts == null
          || retryMaxAttempts.length == 0
          || isNaN(retryMaxAttempts)
          || retryMaxAttempts < 0) {
          this.props.showInputValidationError("Retry max attempts must be a number >= 0!");
        }

        this.props.updateRetryFormMaximumAttempts(retryMaxAttempts);
        this.forceUpdate();
        break;
      }


      case "workflow-name": {
        workflowDef.workflow.name = event.target.value;
        if (workflowDef.workflow.name == null
          || workflowDef.workflow.name.length == 0) {
          this.props.showInputValidationError("The workflow name must not be null or empty!");
        }
        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
      case "workflow-root-address": {
        workflowDef.workflow.metadata.rootAddress = event.target.value;
        if (workflowDef.workflow.metadata.rootAddress == null
          || workflowDef.workflow.metadata.rootAddress.length == 0) {
          this.props.showInputValidationError("The crawling root address must not be null or empty!");
        }

        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
      case "workflow-url-matcher": {
        workflowDef.workflow.metadata.urlMatcher = event.target.value;
        if (workflowDef.workflow.metadata.urlMatcher == null
          || workflowDef.workflow.metadata.urlMatcher.length == 0) {
          this.props.showInputValidationError("The crawling URL matcher must not be null or empty!");
        }

        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
      case "workflow-max-pages-per-depth-level": {
        workflowDef.workflow.metadata.maxPagesPerDepthLevel = event.target.value;
        if (workflowDef.workflow.metadata.maxPagesPerDepthLevel == null
          || workflowDef.workflow.metadata.maxPagesPerDepthLevel.length == 0
          || isNaN(workflowDef.workflow.metadata.maxPagesPerDepthLevel)
          || workflowDef.workflow.metadata.maxPagesPerDepthLevel < 1) {
          this.props.showInputValidationError("The crawling max pages per depth level must be a number >= 1!");
        }

        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
      case "workflow-max-page-size-bytes": {
        workflowDef.workflow.metadata.maxWebPageSizeBytes = event.target.value;
        if (workflowDef.workflow.metadata.maxWebPageSizeBytes == null
          || workflowDef.workflow.metadata.maxWebPageSizeBytes.length == 0
          || isNaN(workflowDef.workflow.metadata.maxWebPageSizeBytes)
          || workflowDef.workflow.metadata.maxWebPageSizeBytes < 1) {
          this.props.showInputValidationError("The crawling max web page size (in bytes) must be a number >= 1!");
        }

        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
      case "workflow-parallel-crawlers": {
        workflowDef.workflow.metadata.maxConcurrentCrawlers = event.target.value;
        if (workflowDef.workflow.metadata.maxConcurrentCrawlers == null
          || workflowDef.workflow.metadata.maxConcurrentCrawlers.length == 0
          || isNaN(workflowDef.workflow.metadata.maxConcurrentCrawlers)
          || workflowDef.workflow.metadata.maxConcurrentCrawlers < 1) {
          this.props.showInputValidationError("The number of parallel crawlers must be >= 1!");
        }

        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
      case "workflow-max-depth": {
        workflowDef.workflow.metadata.maxRecursionDepth = event.target.value;
        if (workflowDef.workflow.metadata.maxRecursionDepth == null
          || workflowDef.workflow.metadata.maxRecursionDepth.length == 0
          || isNaN(workflowDef.workflow.metadata.maxRecursionDepth)
          || workflowDef.workflow.metadata.maxRecursionDepth < 0) {
          this.props.showInputValidationError("The workflow's maximum depth must be >= 0!");
        }

        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
      case "workflow-crawler-timeout": {
        workflowDef.workflow.metadata.crawlerTimeoutSeconds = event.target.value;
        if (workflowDef.workflow.metadata.crawlerTimeoutSeconds == null
          || workflowDef.workflow.metadata.crawlerTimeoutSeconds.length == 0
          || isNaN(workflowDef.workflow.metadata.crawlerTimeoutSeconds)
          || workflowDef.workflow.metadata.crawlerTimeoutSeconds < 15
          || workflowDef.workflow.metadata.crawlerTimeoutSeconds > 115) {
          this.props.showInputValidationError("The crawler timeout (in seconds) must be between 15 and 115 seconds!");
        }
        this.props.updateNewWorkflowDefinition(workflowDef);
        this.forceUpdate();
        break;
      }
    }
  }

  handleRetrierSelectValueChanged(error) {
    this.props.updateCurrentSelectedFormError(error);
  }

  handleAddNewCrawlerRetrier() {
    let intervalSeconds = this.props.formRetryInterval;
    let backoffRate = this.props.formRetryBackoffRate;
    let maxAttempts = this.props.formRetryMaximumAttempts;

    if (intervalSeconds == null
      || intervalSeconds.length == 0
      || isNaN(intervalSeconds)
      || intervalSeconds < 0) {
      this.props.showInputValidationError("Retry interval seconds must be a valid number >= 0!");
      return;
    }
    if (backoffRate == null
      || backoffRate.length == 0
      || isNaN(backoffRate)
      || backoffRate < 1) {
      this.props.showInputValidationError("Retry backoff rate must be a number >= 1!");
      return;
    }
    if (maxAttempts == null
      || maxAttempts.length == 0
      || isNaN(maxAttempts)
      || maxAttempts < 0) {
      this.props.showInputValidationError("Retry max attempts must be a number >= 0!");
      return;
    }

    let updatedMetadata = this.props.newWorkflowDefinition.workflow.metadata;
    updatedMetadata.crawlerRetryPolicy.exponentialBackoffRetriers.push({
      retryAllErrors: this.props.retryAllErrorsTrueRadioButtonChecked,
      errors: this.props.formErrorsToRetry,
      intervalSeconds: this.props.formRetryInterval,
      backoffRate: this.props.formRetryBackoffRate,
      maxAttempts: this.props.formRetryMaximumAttempts
    });
    this.props.newWorkflowDefinition.workflow.metadata = updatedMetadata;
    this.props.updateNewWorkflowDefinition(this.props.newWorkflowDefinition);
    this.props.hideCrawlerRetriersInputForm();
    this.forceUpdate();
  }

  handleAddTextSelector() {
    let textSelectorInput = this.textSelectorInput.refs.input.value;
    if (textSelectorInput == null
      || textSelectorInput.length == 0) {
      this.props.showInputValidationError("The text selector must not be null or empty!");
      return;
    }

    let textSelector = {
      textMatcher: textSelectorInput,
      extractionPolicies: []
    };

    this.props.newWorkflowDefinition.workflow.metadata.selectionPolicy.textSelectors.push(textSelector);
    this.props.updateNewWorkflowDefinition(this.props.newWorkflowDefinition);
    this.props.hideCrawlerTextSelectorsInputForm();
    this.forceUpdate();
  }

  handleAddCSSSelector() {
    let cssSelectorInput = this.cssSelectorInput.refs.input.value;
    if (cssSelectorInput == null
      || cssSelectorInput.length == 0) {
      this.props.showInputValidationError("The CSS selector must not be null or empty!");
      return;
    }

    let cssSelector = {
      cssSelector: cssSelectorInput,
      extractionPolicies: []
    };

    this.props.newWorkflowDefinition.workflow.metadata.selectionPolicy.cssSelectors.push(cssSelector);
    this.props.updateNewWorkflowDefinition(this.props.newWorkflowDefinition);
    this.props.hideCrawlerCSSSelectorsInputForm();
    this.forceUpdate();
  }

  render() {

    const rowStyle = Object.assign({}, textCenter, {
      display: 'block',
      padding: 30,
      paddingTop: 0
    });
    const h2Style = {
      fontSize: '1.5em',
      marginBottom: 30
    };
    const btnStyle = {
      cursor: 'default'
    };
    const leftColStyle = {
      borderRight: '1px solid gainsboro',
      paddingRight: 30,
      paddingLeft: 10,
      paddingTop: 10,
      paddingBottom: 10
    };
    const codeFormatDivStyle = {
      fontSize: '0.75em',
      textAlign: 'left'
    };
    const formFieldStyle = {
      width: '100%',
      margin: 0
    };
    const crawlerRetriersInputFormStyle = {
      display: this.props.crawlerRetriersInputFormVisible ? "block" : "none",
      textAlign: 'left'
    };
    const textSelectorsInputFormStyle = {
      display: this.props.textSelectorsInputFormVisible ? "block" : "none",
      textAlign: 'left'
    };
    const cssSelectorsInputFormStyle = {
      display: this.props.cssSelectorsInputFormVisible ? "block" : "none",
      textAlign: 'left'
    };
    const errorListContainerVisibleStyle = {
      display: this.props.retryAllErrorsFalseRadioButtonChecked ? "block" : "none"
    };
    const paddedLeftDivStyle = {paddingLeft:20};
    const noPaddingLeftStyle = {paddingLeft:0};

    return (
      <div>
        <ReactTooltip multiline={true}/>
        <Row>
          <Col sm="3/5" md="2/3" lg="1/2" style={leftColStyle}>
            <Row style={rowStyle}>
              <h2 style={h2Style}>Construct the workflow's defintion</h2>

              <Form style={{textAlign: 'left'}}>
                <ElementalFormTextInput
                  label="Workflow name"
                  name="workflow-name"
                  dataTip="A friendly name associated to the workflow"
                  placeholder="Friendly name for your workflow"
                  onBlur={(e) => this.handleInputChange(e, "workflow-name")}/>

                <ElementalFormTextInput
                  label="Root address"
                  name="workflow-root-addres"
                  dataTip="The URL from which the crawling process is to be started"
                  placeholder="The starting URL for the crawler"
                  onBlur={(e) => this.handleInputChange(e, "workflow-root-address")}/>

                <ElementalFormTextInput
                  label="URL matcher"
                  name="workflow-url-matcher"
                  dataTip="A regular expression used to select what links are followed<br/> throughout the crawling process. Any link URL that is not matched by <br/>the regular expression will not be crawled"
                  placeholder="Valid RegExp for matching URLs during crawling"
                  onBlur={(e) => this.handleInputChange(e, "workflow-url-matcher")}/>

                <ElementalFormTextInput
                  label="Maximum pages per depth level"
                  name="workflow-max-pages-per-depth-level"
                  dataTip="The maximum number of pages that will be crawled on a certain depth level.<br/> For example, if the rootAddress, which resides on the depth level 0<br/> has N links to other web pages, then the N web-pages will all be crawled only <br/> if maxPagesPerDepthLevel >= N"
                  placeholder="Maximum pages per crawl depth level"
                  onBlur={(e) => this.handleInputChange(e, "workflow-max-pages-per-depth-level")}/>

                <ElementalFormTextInput
                  label="Maximum web page size in bytes"
                  name="workflow-max-page-size-bytes"
                  dataTip="The maximum number of bytes that a web page may have in order to be crawled."
                  placeholder="The maximum web page size (in bytes)"
                  onBlur={(e) => this.handleInputChange(e, "workflow-max-page-size-bytes")}/>

                <ElementalFormTextInput
                  label="Number of parallel crawlers"
                  name="workflow-parallel-crawlers"
                  dataTip="The number of parallel crawlers that may be executed for a certain depth level.<br/> This is important in order to limit the amount of concurrent transactions to the <br/> database and control the implicit costs of having the crawler running. <br/> If this is set to a higher value, then the crawling process will finish faster <br/> (assuming enough read/write capacity units are set on the database), but the <br/> costs will be higher."
                  placeholder="The number of concurrent crawlers"
                  onBlur={(e) => this.handleInputChange(e, "workflow-parallel-crawlers")}/>

                <ElementalFormTextInput
                  label="Maximum recursion depth"
                  name="workflow-max-depth"
                  dataTip="The maximum depth which can be reached by subsequently following unvisited<br/> links throughout the crawling process. The recursion depth is defined as<br/> the distance between the {@link #rootAddress} and the current address that<br/> is being crawled."
                  placeholder="Maximum recursion depth"
                  onBlur={(e) => this.handleInputChange(e, "workflow-max-depth")}/>

                <ElementalFormTextInput
                  label="Crawler timeout in seconds"
                  name="workflow-crawler-timeout"
                  dataTip=" The maximum time, in seconds,<br/> that an instance of a web page crawler is allowed to run."
                  placeholder="Crawler timeout in seconds"
                  onBlur={(e) => this.handleInputChange(e, "workflow-crawler-timeout")}/>
              </Form>

              <div style={{textAlign:'left'}}>
                <FormField label="Crawler exponential backoff retriers"/>
                <div style={paddedLeftDivStyle}>
                  {
                    this.props.newWorkflowDefinition.workflow.metadata
                    .crawlerRetryPolicy.exponentialBackoffRetriers.map((retrier, key) => {
                      return (
                        <ExponentialBackoffRetrierCard
                          retrier={retrier}
                          key={key}
                          deleteButtonSize="xs"
                          onDeleteIconClick={(e) => {
                            this.props.removeExponentialBackoffRetrier(
                              this.props.newWorkflowDefinition,
                              key);
                            this.forceUpdate();
                          }} />
                      );
                    })
                  }
                </div>
                <Button
                  onClick={this.props.showCrawlerRetriersInputForm}
                   type="link-primary"
                   style={noPaddingLeftStyle}>
                  Add (more) crawler retriers
                </Button>
                <div className="well" style={crawlerRetriersInputFormStyle}>
                  <Form>
                    <FormField label="Retry all errors" style={this.formFieldStyle} />
                    <div className="inline-controls">
                      <Radio
                        name="retry-all-errors-true"
                        label="True"
                        checked={this.props.retryAllErrorsTrueRadioButtonChecked}
                        ref={(input) => { this.retryAllErrorsTrueRadioButton = input; }}
                        onChange={(e) => this.handleRetryAllErrorsTrueRadioButtonClick()}
                      />
                      <Radio
                        name="retry-all-errors-false"
                        label="False"
                        checked={this.props.retryAllErrorsFalseRadioButtonChecked}
                        ref={(input) => { this.retryAllErrorsFalseRadioButton = input; }}
                        onChange={(e) => this.handleRetryAllErrorsFalseRadioButtonClick()}
                      />
                    </div>
                    <div style={errorListContainerVisibleStyle}>
                      <FormField label="Errors to retry" style={this.formFieldStyle} />
                      <InputGroup contiguous>
                        <InputGroup.Section grow>
                          <FormSelect
                            options={Utility.getStepFunctionsErrorsForSelect()}
                            firstOption="Choose an AWS Step Functions error"
                            onChange={(e) => this.handleRetrierSelectValueChanged(e)}/>
                        </InputGroup.Section>
                        <InputGroup.Section>
                          <Button onClick={(e) => {
                            if (this.props.currentSelectedFormError == null) {
                              return;
                            }
                            if (this.props.formErrorsToRetry.length > 0) {
                              if (this.props.formErrorsToRetry.indexOf(this.props.currentSelectedFormError) >= 0) {
                                return;
                              }
                            }
                            this.props.formErrorsToRetry.push(
                              this.props.currentSelectedFormError);
                            this.props.updateFormErrorsToRetry(
                              this.props.formErrorsToRetry);
                            this.forceUpdate();
                          }}>
                            Add
                          </Button>
                        </InputGroup.Section>
                      </InputGroup>

                      {
                        this.props.formErrorsToRetry.map((error, key) => {
                          return (
                            <CardWithDeleteButton
                              content={error}
                              key={key}
                              onDeleteIconClick={(e) => {
                                this.props.removeFormErrorToRetry(
                                  this.props.formErrorsToRetry, key);
                                this.forceUpdate();
                              }}
                              deleteButtonSize="xs"/>
                          );
                        })
                      }
                    </div>


                    <ElementalFormTextInput
                      label="First retry after (interval in seconds)"
                      name="retry-interval-seconds"
                      dataTip="The number of seconds to wait before the first retry attempt."
                      placeholder="First retry after (in seconds)"
                      onBlur={(e) => this.handleInputChange(e, "retry-interval-seconds")}/>
                    <ElementalFormTextInput
                      label="Retry backoff rate"
                      name="retry-backoff-rate"
                      dataTip="The rate at which the delay between retries is increased. Calculated as follows:<br/> new_delay = previous_delay * backoffRate, if a previous_delay exists<br/>new_delay = intervalSeconds, if a previous_delay does not exist"
                      placeholder="Retry backoff rate (in seconds)"
                      onBlur={(e) => this.handleInputChange(e, "retry-backoff-rate")}/>
                    <ElementalFormTextInput
                      label="Maximum number of retries"
                      name="retry-max-attempts"
                      dataTip="The maximum number of attempts retry the failed task."
                      placeholder="Maximum number of retry attempts"
                      onBlur={(e) => this.handleInputChange(e, "retry-max-attempts")}/>


                    <Button
                      onClick={(e) => this.handleAddNewCrawlerRetrier()}
                      type="primary">
                      Add crawler retry policy
                    </Button>
                    <Button
                      onClick={(e) => this.props.hideCrawlerRetriersInputForm()}
                      type="link-cancel">
                      Cancel
                    </Button>
                  </Form>
                </div>

                <hr />
                <FormField
                  label="Crawler text selectors"
                  data-tip="A text selector encapsulates logic about selecting <br/>text from a crawled web page based on regexp matching."
                />
                <div style={paddedLeftDivStyle}>
                  {
                    this.props.newWorkflowDefinition.workflow.metadata.selectionPolicy.textSelectors
                    .map((textSelector, key) => {
                      return (
                        <CardWithDeleteButton
                          content={textSelector.textMatcher}
                          key={key}
                          onDeleteIconClick={(e) => {
                            this.props.removeTextSelector(
                              this.props.newWorkflowDefinition, key);
                            this.forceUpdate();
                          }}
                          deleteButtonSize="xs"/>
                      );
                    })
                  }
                </div>
                <Button
                  onClick={this.props.showCrawlerTextSelectorsInputForm}
                  type="link-primary"
                  style={noPaddingLeftStyle}>
                  Add (more) text selectors
                </Button>
                <div className="well" style={textSelectorsInputFormStyle}>
                  <Form>

                    <ElementalFormTextInput
                      label="Text selector"
                      name="text-selector"
                      dataTip="A regular expression defining how text will be extracted from the crawled web pages"
                      placeholder="A valid regular expression as a text selector"
                      refName={(input) => { this.textSelectorInput = input; }}
                      onBlur={(e) => /*Handle regexp validation*/ true}/>

                    <Button
                      onClick={(e) => this.handleAddTextSelector()}
                      type="primary">
                      Add text selector
                    </Button>
                    <Button
                      onClick={(e) => this.props.hideCrawlerTextSelectorsInputForm()}
                      type="link-cancel">
                      Cancel
                    </Button>

                  </Form>
                </div>

                <hr />
                <FormField
                  label="Crawler CSS selectors"
                  data-tip="A CSS selector encapsulates logic about selecting <br/>text from a crawled web page based on CSS selectors."
                />
                <div style={paddedLeftDivStyle}>
                  {
                    this.props.newWorkflowDefinition.workflow.metadata.selectionPolicy.cssSelectors
                    .map((cssSelector, key) => {
                      return (
                        <CardWithDeleteButton
                          content={cssSelector.cssSelector}
                          key={key}
                          onDeleteIconClick={(e) => {
                            this.props.removeCSSSelector(
                              this.props.newWorkflowDefinition, key);
                            this.forceUpdate();
                          }}
                          deleteButtonSize="xs"/>
                      );
                    })
                  }
                </div>
                <Button
                   onClick={this.props.showCrawlerCSSSelectorsInputForm}
                   type="link-primary"
                   style={noPaddingLeftStyle}>
                  Add (more) CSS selectors
                </Button>
                <div className="well" style={cssSelectorsInputFormStyle}>
                  <Form>
                    <ElementalFormTextInput
                      label="CSS selector"
                      name="css-selector"
                      dataTip="A valid css selector to use for extracting data from the crawled web pages"
                      placeholder="A valid CSS selector as a content selector"
                      refName={(input) => { this.cssSelectorInput = input; }}
                      onBlur={(e) => /*Handle regexp validation*/ true}/>
                    <Button
                      onClick={(e) => this.handleAddCSSSelector()}
                      type="primary">
                      Add CSS selector
                    </Button>
                    <Button
                      onClick={(e) => this.props.hideCrawlerCSSSelectorsInputForm()}
                      type="link-cancel">
                      Cancel
                    </Button>
                  </Form>
                </div>
              </div>

              <hr />
              <Form style={{textAlign: 'left'}}>
                <Loader
                  show={this.props.newWorkflowCategorySpinnerActive}
                  contentBlur={7} backgroundStyle={{
                    backgroundColor: 'rgba(0, 0, 0, 0.0'
                  }}
                  message={< Spinner size='md' type='inverted'/>}
                >
                  <Button
                    style={{width:'100%'}}
                    type="primary"
                    onClick={(e) => this.props.createWorkflow(
                      this.props.apigClient,
                      this.props.newWorkflowDefinition
                    )}>
                    Create workflow
                  </Button>
                </Loader>
              </Form>


            </Row>
          </Col>
          <Col sm="2/5" md="1/3" lg="1/2" style={{
            padding: 10
          }}>
            <Row style={rowStyle}>
              <h2 style={h2Style}>Preview definition as JSON</h2>
              <div style={codeFormatDivStyle}>
                <Highlight className='javascript'>
                  {JSON.stringify(this.props.newWorkflowDefinition, null, 2)}
                </Highlight>
              </div>
            </Row>
          </Col>
        </Row>
      </div>
    );
  };
}

NewWorkflow.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired})
};

export default NewWorkflow;
