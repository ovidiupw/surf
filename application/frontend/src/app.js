import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {HashRouter as Router, Route, Redirect, Switch} from 'react-router-dom';
import configureStore from 'redux/store';
import Home from 'containers/Home';
import Dashboard from 'containers/Dashboard';
import App from 'components/App';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';

Utility.requireGlobalErrorHandler();
Utility.requireStyles();
Utility.requireApiGatewayResources();
Utility.initFacebook();

let initialState = {};

const store = configureStore(initialState);

ReactDOM.render((
  <Provider store={store}>
    <Router>
      <App>
        <Switch>
          <Route exact path={Routes.HOME} component={Home}/>
          <Route path={Routes.DASHBOARD} component={Dashboard}/>
          <Route path={Routes.ANY} component={Home}/>
        </Switch>
      </App>
    </Router>
  </Provider>
), document.getElementById('app'));
