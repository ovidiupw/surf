import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {
  Button, Modal, ModalHeader, ModalBody, ModalFooter, Spinner
} from 'elemental';

class S3LinkModal extends React.Component {

  render() {

    let wordWrapBreakWordStyle = {
      wordWrap: 'break-word',
      textAlign: 'center'
    };
    let spinnerDivStyle = {
      textAlign: 'center',
      display: this.props.spinnerActive ? 'block' : 'none'
    };
    let linkStyle = {
      textAlign: 'center',
      display: this.props.spinnerActive ? 'none' : 'block'
    };

    return (
      <div>
        <Modal
          isOpen={this.props.visible}
          onCancel={this.props.closeHandler}
          backdropClosesModal>
          <ModalHeader
            style={wordWrapBreakWordStyle}
            text={this.props.header}
            showCloseButton
            onClose={this.props.closeHandler}/>
          <ModalBody
            style={wordWrapBreakWordStyle}>
            <div style={spinnerDivStyle}>
              <Spinner size="md" type="primary" />
            </div>
            <a
              style={linkStyle}
              href={this.props.cssLink}
              target="_blank">
              Get CSS Data
            </a>
            <a
              style={linkStyle}
              href={this.props.textLink}
              target="_blank">
              Get Textual Data
            </a>
          </ModalBody>
          <ModalFooter
            style={wordWrapBreakWordStyle}>
            <Button type="link-cancel" onClick={this.props.closeHandler}>
              Dismiss
            </Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  };
}

export default S3LinkModal;
