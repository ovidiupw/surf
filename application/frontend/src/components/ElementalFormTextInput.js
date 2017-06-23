import React from 'react';
import {PropTypes} from 'prop-types';
import {textCenter} from 'styles/generic-styles';
import {
  Col,
  FormRow,
  FormField,
  FormIconField,
  FormInput
} from 'elemental';

class ElementalFormTextInput extends React.Component {

  constructor() {
    super();
    this.formFieldStyle = {
      width: '100%',
      margin: 0
    };
  }

  render() {
    return (
      <FormRow>
        <FormField
          style={this.formFieldStyle}
          label={this.props.label}
          htmlFor={this.props.name}>
          <FormIconField
            style={this.formFieldStyle}
            data-tip={this.props.dataTip}
            iconPosition="left"
            iconColor="default"
            iconKey="question">
            <FormInput
              onBlur={this.props.onBlur}
              onChange={this.props.onChange}
              placeholder={this.props.placeholder}
              htmlFor={this.props.name}
              ref={this.props.refName}/>
          </FormIconField>
        </FormField>
      </FormRow>
    );
  }
};

ElementalFormTextInput.propTypes = {
  label: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  dataTip: PropTypes.string.isRequired,
  placeholder: PropTypes.string.isRequired
};

export default ElementalFormTextInput;
