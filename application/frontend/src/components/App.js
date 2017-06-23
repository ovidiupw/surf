import React from 'react';
import SideBarLeft from 'containers/SideBarLeft';
import ErrorModal from 'components/ErrorModal';
import {Row, Col} from 'elemental';
import {textCenter} from 'styles/generic-styles';

class App extends React.Component {
  render() {

    let mainContentSizeData = {
      sm: "3/4",
      md: "4/5",
      lg: "5/6"
    };
    if (!this.props.loggedIn) {
      mainContentSizeData.sm = "";
      mainContentSizeData.md = "";
      mainContentSizeData.lg = "";
    }

    return (
      <div>
        <div style={{margin: 10}} className="container-fluid">
          <ErrorModal
            header={this.props.errorModal.header}
            body={this.props.errorModal.body}
            footer={this.props.errorModal.footer}
            visible={this.props.errorModal.visible}
            closeHandler={this.props.hideErrorModal}/>
          <Row>
            <SideBarLeft visible={this.props.loggedIn} />
            <Col
              sm={mainContentSizeData.sm}
              md={mainContentSizeData.md}
              lg={mainContentSizeData.lg}>
              {this.props.children}
            </Col>
          </Row>
        </div>

      </div>
    );
  }
};

export default App;
