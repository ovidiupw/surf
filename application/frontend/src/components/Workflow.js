import React from 'react';
import {textCenter} from 'styles/generic-styles';
import {Spinner, Col, Row, Button, Glyph} from 'elemental';
import Loader from 'react-loader-advanced';
import ReactTable from 'react-table';
import {NavLink} from 'react-router-dom';
import Routes from 'constants/Routes';
import {RESULTS_PER_PAGE} from 'constants/Configs';

let Highlight = require('react-highlight');

class Workflow extends React.Component {

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount(this.props.apigClient);
    let that = this;
    setTimeout(function() {
      if (that.props.apigClient == null) {
        that.props.gotoHome();
      } else {
        that.props.getWorkflow(
          that.props.apigClient,
          that.props.workflowId
        );
        that.props.getWorkflowExecutions(
          that.props.apigClient,
          that.props.workflowId
        );
      }
    }, 100);
  }

  render() {
    let workflowId = this.props.workflowId;

    const tableData = this.props.workflowExecutions;
    const tableColumns = [
      {
        Header: 'Creation date',
        id: 'creationDate',
        accessor: execution => {
          console.log(execution.creationDateMillis);
          return new Date(execution.creationDateMillis).toString();
        }
      }, {
        Header: 'Minutes elapsed',
        id: "minutesElapsed",
        accessor: execution => {
          let millisElapsed = 1;
          if (execution.endDateMillis == 0) {
            millisElapsed = Math.abs(
              new Date(execution.startDateMillis) - new Date());
          } else {
            millisElapsed = Math.abs(
              new Date(execution.startDateMillis) - new Date(execution.endDateMillis));
          }

          return Math.ceil(millisElapsed / 1000 / 60);
        },
        Cell: props => {
          let execution = props.row;
          return (
            <span>
              ~ {execution.minutesElapsed}
            </span>
          );
        }
      }, {
        Header: 'Status',
        accessor: 'status',
        id: 'status'
      }, {
        Header: 'Actions',
        id: 'actions',
        Cell: props => {
          let execution = props.row;
          return (
            <NavLink to={Routes.WORKFLOW_EXECUTIONS
              .replace(":workflow_id", this.props.workflowId)
              .replace(":id", execution.id)}>
                View execution
            </NavLink>
          );
        }
      }, {
        Header: 'Id',
        accessor: 'id',
        id: 'id'
      }
    ];

    console.log(tableData);
    console.log(tableColumns);

    const leftColStyle = {
      borderRight: '1px solid gainsboro',
      paddingRight: 30,
      paddingLeft: 10,
      paddingTop: 10,
      paddingBottom: 10
    };
    const rightColStyle = {
      paddingRight: 10,
      paddingLeft: 30,
      paddingTop: 10,
      paddingBottom: 10
    };
    const h2StyleLeft = {
      fontSize: '1.1em',
      marginBottom: 30,
      textAlign: 'center'
    };
    const h2StyleRight = {
      fontSize: '1.1em',
      marginBottom: 30,
      textAlign: 'center',
      textStyle: 'bold'
    };
    const codeFormatDivStyle = {
      fontSize: '0.75em',
      textAlign: 'left',
    };
    const spinnerDivStyle = {
      textAlign: 'center',
      paddingBottom: 20,
      display: this.props.loadSpinnerVisible ? 'block' : 'none'
    };
    const executionsSpinnerDivVisible = {
      textAlign: 'center',
      paddingBottom: 20,
      display: this.props.executionsSpinnerVisible ? 'block' : 'none'
    };

    let workflowName = this.props.workflowId;
    if (this.props.workflow != null) {
      workflowName = this.props.workflow.name;
    }

    return (
      <div>
        <Row>
          <Col sm="1/2" md="1/3" lg="1/4" style={leftColStyle}>
            <h2 style={h2StyleLeft}>
              <p>Workflow definition for</p>
              <div
                className="well"
                style={{marginTop:10, paddingTop:5, paddingBottom:5}}>
                {workflowName}
              </div>
            </h2>

            <div style={{textAlign:'center', marginBottom:20}}>
              <Loader
                show={this.props.startSpinnerVisible || this.props.loadSpinnerVisible}
                contentBlur={7} backgroundStyle={{
                  backgroundColor: 'rgba(0, 0, 0, 0.0'
                }}
                message={< Spinner size='md' type='inverted'/>}
              >
                <Button
                  onClick={(e) => this.props.startWorkflow(
                    this.props.apigClient,
                    this.props.workflowId
                  )}
                  type="primary"
                  style={{width:'100%'}}>
                  Start workflow
                </Button>
              </Loader>
            </div>

            <div style={codeFormatDivStyle}>
              <div style={spinnerDivStyle}>
                <Spinner size="md" type="primary" />
              </div>
              <Highlight className='javascript'>
                {JSON.stringify(this.props.workflow, null, 2)}
              </Highlight>
            </div>
          </Col>
          <Col sm="1/2" md="2/3" lg="3/4" style={rightColStyle}>
            <h2 style={h2StyleRight}>
              <p>Workflow executions</p>
              <b style={{marginTop:10}}></b>
            </h2>

            <div style={executionsSpinnerDivVisible}>
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
  }
};

export default Workflow;
