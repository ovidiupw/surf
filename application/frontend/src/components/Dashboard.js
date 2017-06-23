import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {Button, Card, Glyph, Row, Col} from 'elemental';

class Dashboard extends React.Component {

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount(this.props.apigClient);
    let that = this;
    setTimeout(function() {
      if (that.props.apigClient == null) {
        that.props.gotoHome();
      }
    }, 100);
  }

  render() {
    return (
      <div>
        <Card>
          <Row>
            <Col sm="1/20">
              <Glyph icon="light-bulb"/>
            </Col>
            <Col sm="19/20">
              <p style={textCenter}>
                Welcome to your dashboard! Here you can review metrics about current crawling workflows' executions and other general data about your crawler. You can also select one of the categories on the left side of the page in order to navigate to its page.
              </p>
            </Col>
          </Row>
        </Card>
        <Button onClick={(e) => this.props.workersGet(this.props.apigClient)}>
          Call workersGet()
        </Button>
      </div>
    );
  };
}

Dashboard.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired})
};

export default Dashboard;
