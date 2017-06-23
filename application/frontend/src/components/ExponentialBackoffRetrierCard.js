import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {
  Button,
  Card,
  Row,
  Col,
  Glyph

} from 'elemental';

class RetrierEntry extends React.Component {
  render() {
    return (
      <Row data-tip={this.props.dataTip} style={{
        color: '#777'
      }}>
        <Col sm="1/2">
          <b>
            {this.props.entryName}
          </b>
        </Col>
        <Col sm="1/2">
          {this.props.value}
        </Col>
      </Row>
    );
  }
}

class RetrierArrayEntry extends React.Component {
  render() {
    return (
      <Row data-tip={this.props.dataTip} style={{
        color: '#777'
      }}>
        <Col sm="1/2">
          <b>
            {this.props.entryName}
          </b>
        </Col>
        <Col sm="1/2">
          {this.props.values.map((value, key) => {
            return (
              <Row key={key}>
                {value}
              </Row>
            );
          })}
        </Col>
      </Row>
    );
  }
}

class ExponentialBackoffRetrierCard extends React.Component {

  constructor() {
    super();
    this.hrStyle={
      margin:0,
      marginTop:4,
      marginBottom:4
    };
  }

  render() {
    return (
      <Card key={this.props.key}>
        <Row>
          <Col xs="3/4" sm="6/7" md="6/7">
            <RetrierEntry
              entryName="Interval seconds"
              dataTip="The number of seconds to wait before the first retry attempt."
              value={this.props.retrier.intervalSeconds} />
            <hr style={this.hrStyle}/>
            <RetrierEntry
              entryName="Backoff rate"
              dataTip="The rate at which the delay between retries is increased. Calculated as follows:<br/> new_delay = previous_delay * backoffRate, if a previous_delay exists<br/>new_delay = intervalSeconds, if a previous_delay does not exist"
              value={this.props.retrier.backoffRate} />
            <hr style={this.hrStyle}/>
            <RetrierEntry
              entryName="Maximum attempts"
              dataTip="The maximum number of attempts retry the failed task."
              value={this.props.retrier.maxAttempts} />
            <hr style={this.hrStyle}/>
            {this.props.retrier.retryAllErrors
              ?
                <RetrierEntry
                  entryName="Retry all errors"
                  dataTip="Flag indicating if all errors should be retried. The definition of {@link #errors} becomes irrelevant<br/> if this is set to true."
                  value={this.props.retrier.retryAllErrors.toString()} />
              :
                <RetrierArrayEntry
                  entryName="Errors to retry"
                  dataTip="The names of the errors which should be retried by using this retrier.<br/>The name of the errors represent fully qualified Java class names."
                  values={this.props.retrier.errors} />
            }

            <hr style={this.hrStyle}/>
          </Col>

          <Col style={{textAlign:'center'}} xs="1/4" sm="1/7" md="1/7">
            <Button size={this.props.deleteButtonSize} onClick={this.props.onDeleteIconClick} >
              <Glyph icon="trashcan" />
            </Button>
          </Col>

        </Row>
      </Card>
    );
  };
}

ExponentialBackoffRetrierCard.propTypes = {
  retrier: PropTypes.shape({
    retryAllErrors: PropTypes.bool,
    errors: PropTypes.arrayOf(PropTypes.string),
    intervalSeconds: PropTypes.number.isRequired,
    backoffRate: PropTypes.number.isRequired,
    maxAttempts: PropTypes.number.isRequired,
  }).isRequired,
  onDeleteIconClick: PropTypes.func.isRequired,
  deleteButtonSize: PropTypes.string.isRequired
};

export default ExponentialBackoffRetrierCard;
