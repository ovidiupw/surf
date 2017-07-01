import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {Button, Spinner, Row, Col} from 'elemental';
import {RESULTS_PER_PAGE} from 'constants/Configs';
import ReactTable from 'react-table';

class WorkflowExecutions extends React.Component {

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount(this.props.apigClient);
    let that = this;
    setTimeout(function() {
      if (that.props.apigClient == null) {
        that.props.gotoHome();
      } else {
        that.props.getVisitedPages(
          that.props.apigClient,
          that.props.workflowExecutionId
        );
      }
    }, 100);
  }

  render() {
    const tableData = this.props.visitedPages;
    const tableColumns = [
      {
        Header: 'URL',
        accessor: 'url',
        id: 'url',
        Cell: props => {
          return (
            <a href={props.row.url} target="_blank">
            {props.row.url}
            </a>
          );
        }
      }, {
        Header: 'Visit depth',
        accessor: 'pageVisitDepth'
      }, {
        Header: 'Actions',
        id: 'actions',
        Cell: props => {
          let pageVisitData = props.row;
          return (
            <Button
              type="link-primary"
               onClick={(e) => this.props.generateS3Link(
                this.props.apigClient,
                pageVisitData.url,
                this.props.workflowExecutionId
              )}>
                Get data
            </Button>
          );
        }
      },
    ];

    const visitedPagesSpinnerDivStyle = {
      textAlign: 'center',
      paddingBottom: 20,
      display: this.props.visitedPagesSpinnerActive ? 'block' : 'none'
    };

    const h2Style = {
      fontSize: '1.1em',
      marginBottom: 30,
      textAlign: 'center'
    };

    let workflowName = this.props.workflowId;
    if (this.props.workflow != null) {
      workflowName = this.props.workflow.name;
    }

    return (
      <div>
        <Row>
          <Col sm="2/10">
            <Button
              style={{width:'100%', marginTop:22, height:30}}
              onClick={(e) => this.props.getVisitedPages(
                this.props.apigClient,
                this.props.workflowExecutionId
              )}>
              Refresh
            </Button>
          </Col>
          <Col sm="8/10">
            <h2 style={h2Style}>
              <p>Workflow execution details for</p>
              <div
                className="well"
                style={{marginTop:10, paddingTop:5, paddingBottom:5}}>
                {workflowName}
              </div>
            </h2>
          </Col>
        </Row>
        <Row>
          <Col>
            <div style={visitedPagesSpinnerDivStyle}>
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
            </Col>
          </Row>
      </div>
    );
  };
}

WorkflowExecutions.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired})
};

export default WorkflowExecutions;
