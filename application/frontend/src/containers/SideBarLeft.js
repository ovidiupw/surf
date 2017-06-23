import {connect} from 'react-redux';
import SideBarLeft from 'components/SideBarLeft';
import Utility from 'modules/Utility';
import Routes from 'constants/Routes';

function mapStateToProps(state, ownProps) {
  return {
    workflowsCategorySpinnerActive: state.spinner.workflowsCategorySpinnerActive,
    workflowExecutionsCategorySpinnerActive: state.spinner.workflowExecutionsCategorySpinnerActive
  };
}

function mapDispatchToProps(dispatch, ownProps) {
  return {
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(SideBarLeft);
