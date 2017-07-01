import {combineReducers} from 'redux';
import {auth} from 'redux/reducers/Auth';
import {spinner} from 'redux/reducers/Spinner';
import {workflows} from 'redux/reducers/Workflows';
import {errorModal, s3LinkModal} from 'redux/reducers/Modals';
import {newWorkflow} from 'redux/reducers/NewWorkflow';
import {workflow} from 'redux/reducers/Workflow';
export default combineReducers({
  auth,
  spinner,
  workflows,
  errorModal,
  s3LinkModal,
  newWorkflow,
  workflow
});
