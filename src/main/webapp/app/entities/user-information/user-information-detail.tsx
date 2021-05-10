import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-information.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserInformationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserInformationDetail = (props: IUserInformationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { userInformationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userInformationDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.userInformation.detail.title">UserInformation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userInformationEntity.id}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="jhipsterSampleApplicationApp.userInformation.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{userInformationEntity.phone}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.userInformation.user">User</Translate>
          </dt>
          <dd>{userInformationEntity.user ? userInformationEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.userInformation.address">Address</Translate>
          </dt>
          <dd>{userInformationEntity.address ? userInformationEntity.address.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-information" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-information/${userInformationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ userInformation }: IRootState) => ({
  userInformationEntity: userInformation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserInformationDetail);
