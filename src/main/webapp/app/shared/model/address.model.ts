export interface IAddress {
  id?: number;
  number?: string | null;
  road?: string | null;
  zipCode?: string | null;
  town?: string | null;
}

export const defaultValue: Readonly<IAddress> = {};
