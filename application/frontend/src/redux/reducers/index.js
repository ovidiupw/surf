import {combineReducers} from 'redux';
import {auth} from 'redux/reducers/Auth';
import {spinner} from 'redux/reducers/Spinner';
import {workflows} from 'redux/reducers/Workflows';
import {errorModal} from 'redux/reducers/Modals';
import {newWorkflow} from 'redux/reducers/NewWorkflow';
export default combineReducers({
  auth,
  spinner,
  workflows,
  errorModal,
  newWorkflow
});
