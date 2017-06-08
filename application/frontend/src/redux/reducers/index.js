import {combineReducers} from 'redux';
import {auth} from 'redux/reducers/auth';
import {spinner} from 'redux/reducers/spinner';

export default combineReducers({auth, spinner});
