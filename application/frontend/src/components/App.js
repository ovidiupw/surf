import React from 'react';
import NavBarTop from 'containers/NavBarTop';

class App extends React.Component {
  render() {
    return (
      <div>
        <NavBarTop />

        {/* Now add the passed prop content */}
        <div className="container-fluid">
          {this.props.children}
        </div>
      </div>
    );
  }
};

export default App;
