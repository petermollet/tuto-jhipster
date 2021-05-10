import { IUser } from 'app/shared/model/user.model';
import { IAddress } from 'app/shared/model/address.model';

export interface IUserInformation {
  id?: number;
  phone?: string | null;
  user?: IUser;
  address?: IAddress;
}

export const defaultValue: Readonly<IUserInformation> = {};
