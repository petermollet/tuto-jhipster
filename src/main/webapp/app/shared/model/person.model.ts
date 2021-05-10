import dayjs from 'dayjs';
import { IAddress } from 'app/shared/model/address.model';

export interface IPerson {
  id?: number;
  lastName?: string;
  firstName?: string;
  birthday?: string | null;
  address?: IAddress | null;
}

export const defaultValue: Readonly<IPerson> = {};
