import { ReactNode } from 'react';

export type DialogContentProps<T = any> = {
  initialValues?: T;
  onClose: () => void;
};

export type DialogProps<T = any> = Omit<DialogContentProps<T>, 'onClose'> & {
  opened: boolean;
  size: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  content: (props: DialogContentProps<T>) => ReactNode;
};

export type DialogContextType = {
  dialog?: DialogProps<any>;
  open: <T = undefined>(dialog: Omit<DialogProps<T>, 'opened'>) => void;
  close: () => void;
};
