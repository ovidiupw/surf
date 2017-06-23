import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {Button} from 'elemental';
import ReactTable from 'react-table';
import {RESULTS_PER_PAGE} from 'constants/Configs';

class Workflows extends React.Component {

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount(this.props.apigClient);
    let that = this;
    setTimeout(function() {
      console.log("Setting timeout");
      if (that.props.apigClient == null) {
        console.log("AKSJDNAKJSDBASKJBDKJS");
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
        Header: 'ID',
        accessor: 'id'
      }, {
        Header: 'Name',
        accessor: 'name' // String-based value accessors!
      }, {
        Header: 'Metadata', // Required because our accessor is not a string
        id: 'metadata',
        accessor: m => JSON.stringify(m)
      }
    ];

    console.log(tableData);
    console.log(tableColumns);

    let that = this;

    return (
      <ReactTable
        style={textCenter}
        data={tableData}
        columns={tableColumns}
        noDataText={'No workflows found!'}
        showPaginationTop={true}
        defaultPageSize={RESULTS_PER_PAGE}
        showPageSizeOptions={false}
        loading={this.props.workflowExecutionsCategorySpinnerActive}
        showPageJump={true}
        onPageChange={(pageIndex) => {
          that.props.workflowsGet(
            that.props.apigClient, that.props.lastWorkflowOnPage);
        }} />
    );
  };
}

Workflows.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired})
};

export default Workflows;
