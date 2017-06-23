import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {
  Button,
  Card,
  Row,
  Col,
  Glyph

} from 'elemental';

class CardWithDeleteButton extends React.Component {

  constructor() {
    super();
    this.hrStyle={
      margin:0,
      marginTop:4,
      marginBottom:4
    };
  }

  render() {
    return (
      <Card key={this.props.key}>
        <Row>
          <Col xs="3/4" sm="6/7" md="6/7">
            {this.props.content}
          </Col>
          <Col style={{textAlign:'center'}} xs="1/4" sm="1/7" md="1/7">
            <Button
              size={this.props.deleteButtonSize}
              onClick={this.props.onDeleteIconClick} >
              <Glyph icon="trashcan" />
            </Button>
          </Col>

        </Row>
      </Card>
    );
  };
}

CardWithDeleteButton.propTypes = {
  content: PropTypes.string.isRequired,
  onDeleteIconClick: PropTypes.func.isRequired,
  deleteButtonSize: PropTypes.string.isRequired
};

export default CardWithDeleteButton;
