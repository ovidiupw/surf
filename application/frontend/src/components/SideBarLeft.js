import React from 'react';
import {Col} from 'elemental';
import {textCenter} from 'styles/generic-styles';
import Routes from 'constants/Routes';
import {NavLink} from 'react-router-dom';
import {Spinner} from 'elemental';

class SideBarLeft extends React.Component {
  render() {

    let sideBarStyle = {
      display: this.props.visible
        ? "block"
        : "none"
    };

    let workflowsCategorySpinnerStyle = {
      display: this.props.workflowsCategorySpinnerActive
        ? "inline-block"
        : "none"
    };

    let workflowExecutionsCategorySpinnerStyle = {
      display: this.props.workflowExecutionsCategorySpinnerActive
        ? "inline-block"
        : "none"
    };

    let newWorkflowCategorySpinnerStyle = {
      display: this.props.newWorkflowCategorySpinnerActive
        ? "inline-block"
        : "none"
    };

    return (
      <Col style={sideBarStyle} sm="1/4" md="1/5" lg="1/6">
        <ul className="nav nav-pills nav-stacked" style={textCenter}>
          <li role="presentation">
            <NavLink to={Routes.NEW_WORKFLOW}>
              New workflow&nbsp;
              <span style={newWorkflowCategorySpinnerStyle}>
                <Spinner type="primary"/>
              </span>
            </NavLink>
          </li>
          <li role="presentation">
            <NavLink to={Routes.WORKFLOWS}>
              Workflows&nbsp;
              <span style={workflowsCategorySpinnerStyle}>
                <Spinner type="primary"/>
              </span>
            </NavLink>
          </li>
          <hr style={{padding:3, margin:3}} />
          <li role="presentation" style={{display:'none'}}>
            {/* make visible after TODO list executions for user */}
            <NavLink to={Routes.WORKFLOW_EXECUTIONS}>
              Executions&nbsp;
            </NavLink>
          </li>
        </ul>
      </Col>

    );
  }
};

export default SideBarLeft;
