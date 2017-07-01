import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {Button, Spinner} from 'elemental';
import ReactTable from 'react-table';
import {NavLink} from 'react-router-dom';
import {RESULTS_PER_PAGE} from 'constants/Configs';
import Routes from 'constants/Routes';

class Workflows extends React.Component {

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount(this.props.apigClient);
    let that = this;
    setTimeout(function() {
      console.log("Setting timeout");
      if (that.props.apigClient == null) {
        that.props.gotoHome();
      } else {
        console.log("Getting workflows");
        that.props.workflowsGet(that.props.apigClient);
      }
    }, 100);
  }

  render() {
    const tableData = this.props.workflows;
    const tableColumns = [
      {
        Header: 'Name',
        accessor: 'name', // String-based value accessors!
        Cell: props => {
          let workflow = props.row;
          return (
            <NavLink to={Routes.WORKFLOW.replace(":id", workflow.id)}>
              {workflow.name}
            </NavLink>
          );
        }
      }, {
        Header: 'Date created', // Required because our accessor is not a string
        id: 'creationDateMillis',
        accessor: workflow => {
          console.log(workflow.creationDateMillis);
          return new Date(workflow.creationDateMillis).toString();
        }
      }, {
        Header: 'ID',
        accessor: 'id'
      }
    ];

    console.log(tableData);
    console.log(tableColumns);

    const workflowsSpinnerDivStyle = {
      textAlign: 'center',
      paddingBottom: 20,
      display: this.props.workflowsCategorySpinnerActive ? 'block' : 'none'
    };


    return (
      <div>
        <div style={workflowsSpinnerDivStyle}>
          <Spinner size="md" type="primary" />
        </div>
        <ReactTable
          style={textCenter}
          data={tableData}
          columns={tableColumns}
          noDataText={'No workflows found!'}
          defaultPageSize={RESULTS_PER_PAGE}
          showPageSizeOptions={false}
          loading={this.props.workflowExecutionsCategorySpinnerActive}
          showPageJump={true} />
        </div>
    );
  };
}

Workflows.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired})
};

export default Workflows;
