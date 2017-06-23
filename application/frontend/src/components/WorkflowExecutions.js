import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {Button, Spinner} from 'elemental';

class WorkflowExecutions extends React.Component {

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
        Workflow executions
      </div>
    );
  };
}

WorkflowExecutions.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired})
};

export default WorkflowExecutions;
