import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {Button, Modal, ModalHeader, ModalBody, ModalFooter} from 'elemental';

class ErrorModal extends React.Component {

  render() {

    let wordWrapBreakWordStyle = {
      wordWrap: 'break-word'
    }

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
            {this.props.body}
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

export default ErrorModal;
