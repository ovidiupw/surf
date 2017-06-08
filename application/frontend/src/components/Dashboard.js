import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';

class Dashboard extends React.Component {

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount();
  }

  render() {
    return (
      <div>
        Hello there, this is the dashboard
      </div>
    );
  };
}

Dashboard.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired})
};

export default Dashboard;
