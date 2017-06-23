import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {Button} from 'elemental';
import Loader from 'react-loader-advanced';
import {Spinner} from 'elemental';

class Home extends React.Component {

  componentDidMount() {
    this.props.lifecycleMethods.componentDidMount(this.props.apigClient);
  }

  render() {
    return (
      <div>
        <div className="jumbotron" style={textCenter}>
          <div style={{
            marginBottom: 40
          }}>
            <h1>Surf</h1>
            <p>On-demand distributed crawling as a service</p>
          </div>
          <Loader show={this.props.showSpinner} contentBlur={7} backgroundStyle={{
            backgroundColor: 'rgba(0, 0, 0, 0.0'
          }} message={< Spinner size='md' type='inverted'/>}>
            <Button size="lg" type="primary" onClick={(e) => this.props.handleAuthViaFacebook()}>
              Authenticate
            </Button>
          </Loader>
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
}

Home.propTypes = {
  lifecycleMethods: PropTypes.shape({componentDidMount: PropTypes.func.isRequired}),
  handleAuthViaFacebook: PropTypes.func.isRequired
};

export default Home;
