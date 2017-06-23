import React from 'react';
import {Link} from 'react-router-dom';
import Routes from 'constants/Routes';

class NavBarTop extends React.Component {

  constructor(props) {
    super(props);
    this.loggedInElementsVisible = {
      display: 'none'
    };
  }

  handleLogout() {
    this.props.handleLogout();
    this.loggedInElementsVisible.display = 'none';
  }

  render() {
    console.log("Redenring NavBarTop...");

    if (this.props.loggedIn) {
      this.loggedInElementsVisible.display = 'block';
    }

    return (
      <div>
        <nav className="navbar navbar-default">
          <div className="container-fluid">
            <div className="navbar-header">
              <Link className="navbar-brand" to={Routes.HOME}>
                <p>Surf</p>
              </Link>
            </div>
            <div className="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
              <ul className="nav navbar-nav">
                <li>
                  <Link to={Routes.DASHBOARD} style={this.loggedInElementsVisible}>
                      Dashboard
                  </Link>
                </li>
              </ul>
              <ul className="nav navbar-nav navbar-right">
                <li>
                  <a href="#" style={this.loggedInElementsVisible}>
                    Singed in as {this.props.userName}
                  </a>
                </li>
                <li>
                  <Link to={Routes.HOME} onClick={e => this.handleLogout()} style={this.loggedInElementsVisible}>
                    Logout
                  </Link>
                </li>
                <li>
                  <a href="" target="_blank">
                    Help
                  </a>
                </li>
              </ul>
            </div>
          </div>
        </nav>
      </div>
    );
  }
}

export default NavBarTop;
