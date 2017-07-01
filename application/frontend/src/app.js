import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {HashRouter as Router, Route, Redirect, Switch} from 'react-router-dom';
import configureStore from 'redux/store';
import Home from 'containers/Home';
import Dashboard from 'containers/Dashboard';
import App from 'containers/App';
import Workflows from 'containers/Workflows';
import NewWorkflow from 'containers/NewWorkflow';
import WorkflowExecutions from 'containers/WorkflowExecutions';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';
import NavBarTop from 'containers/NavBarTop';
import Workflow from 'containers/Workflow';

Utility.requireGlobalErrorHandler();
Utility.requireStyles();
Utility.requireApiGatewayResources();
Utility.initFacebook();

let initialState = {};

const store = configureStore(initialState);

ReactDOM.render((
  <Provider store={store}>
    <Router>
      <div>
        <NavBarTop/>
        <Route exact path={Routes.HOME} component={Home}/>
        <App>
          <Router>
            <div>
              <Route exact path={Routes.DASHBOARD} component={Dashboard}/>
              <Route exact path={Routes.WORKFLOWS} component={Workflows}/>
              <Route exact path={Routes.NEW_WORKFLOW} component={NewWorkflow}/>
              <Route exact path={Routes.WORKFLOW_EXECUTIONS} component={WorkflowExecutions}/>
              <Route exact path={Routes.WORKFLOW} component={Workflow}/>
            </div>
          </Router>
        </App>
      </div>
    </Router>
  </Provider>
), document.getElementById('app'));
