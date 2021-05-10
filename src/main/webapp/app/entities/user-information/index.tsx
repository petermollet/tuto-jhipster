import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserInformation from './user-information';
import UserInformationDetail from './user-information-detail';
import UserInformationUpdate from './user-information-update';
import UserInformationDeleteDialog from './user-information-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserInformationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserInformationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserInformationDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserInformation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserInformationDeleteDialog} />
  </>
);

export default Routes;
