import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {HashRouter as Router, Route} from 'react-router-dom';
import configureStore from 'redux/store';
import Home from 'containers/home';
import Utility from 'modules/utility';

Utility.requireBootstrapResources();
Utility.requireApiGatewayResources();
Utility.initFacebook();

const store = configureStore();

ReactDOM.render((
  <Provider store={store}>
    <Router>
      <Route path="/" component={Home} />
    </Router>
  </Provider>
), document.getElementById('app'));
