import React from 'react';
import {PropTypes} from 'prop-types';
import {connect} from 'react-redux';

import {textCenter} from 'styles/generic-styles';

const Home = ({lifecycleMethods, handleAuthViaFacebook}) => {

  lifecycleMethods.componentDidMount();

  return (
    <div>
      <div className="jumbotron" style={textCenter}>
        <div style={{
          marginBottom: 40
        }}>
          <h1>Surf</h1>
          <p>On-demand distributed crawling as a service</p>
        </div>
        <p>
          <a onClick={handleAuthViaFacebook} className="btn btn-primary btn-lg" href="#" role="button">
            Authenticate
          </a>
        </p>
      </div>
      <div className="container-fluid" style={{
        marginBottom: 50
      }}>
        <div className="row">
          <div className="col-md-2"></div>
          <div className="col-md-8" style={{
            textAlign: 'center'
          }}>
            <hr/>
            <p>
              Press the authenticate button above to authenticate to the platform
            </p>
            <a href="https://github.com/ovidiupw/surf/wiki" target="_blank">
              Learn more about Surf
            </a>
          </div>
          <div className="col-md-2"></div>
        </div>
      </div>
    </div>
  );
};

Home.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired}),
  handleAuthViaFacebook: PropTypes.func.isRequired
};

Home.componentDidMount = function() {
  console.log("Home component did mount!");
};

export default Home;
