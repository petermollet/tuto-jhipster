import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserInformation, defaultValue } from 'app/shared/model/user-information.model';

export const ACTION_TYPES = {
  FETCH_USERINFORMATION_LIST: 'userInformation/FETCH_USERINFORMATION_LIST',
  FETCH_USERINFORMATION: 'userInformation/FETCH_USERINFORMATION',
  CREATE_USERINFORMATION: 'userInformation/CREATE_USERINFORMATION',
  UPDATE_USERINFORMATION: 'userInformation/UPDATE_USERINFORMATION',
  PARTIAL_UPDATE_USERINFORMATION: 'userInformation/PARTIAL_UPDATE_USERINFORMATION',
  DELETE_USERINFORMATION: 'userInformation/DELETE_USERINFORMATION',
  RESET: 'userInformation/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserInformation>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type UserInformationState = Readonly<typeof initialState>;

// Reducer

export default (state: UserInformationState = initialState, action): UserInformationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERINFORMATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERINFORMATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_USERINFORMATION):
    case REQUEST(ACTION_TYPES.UPDATE_USERINFORMATION):
    case REQUEST(ACTION_TYPES.DELETE_USERINFORMATION):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_USERINFORMATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_USERINFORMATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERINFORMATION):
    case FAILURE(ACTION_TYPES.CREATE_USERINFORMATION):
    case FAILURE(ACTION_TYPES.UPDATE_USERINFORMATION):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_USERINFORMATION):
    case FAILURE(ACTION_TYPES.DELETE_USERINFORMATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERINFORMATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERINFORMATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERINFORMATION):
    case SUCCESS(ACTION_TYPES.UPDATE_USERINFORMATION):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_USERINFORMATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERINFORMATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/user-informations';

// Actions

export const getEntities: ICrudGetAllAction<IUserInformation> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_USERINFORMATION_LIST,
  payload: axios.get<IUserInformation>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IUserInformation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERINFORMATION,
    payload: axios.get<IUserInformation>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IUserInformation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERINFORMATION,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserInformation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERINFORMATION,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IUserInformation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_USERINFORMATION,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserInformation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERINFORMATION,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
