
export type DialogContentProps<T = any> = {
  initialValues?: T;
  onClose: () => void;
};

export type DialogProps<T = any> = Omit<DialogContentProps<T>, 'onClose'> & {
  opened: boolean;
  size: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  Content: (props: DialogContentProps<T>) =>  JSX.Element;
};

export type DialogContextType = {
  dialog?: DialogProps<any>;
  open: <T = undefined>(dialog: Omit<DialogProps<T>, 'opened'>) => void;
  close: () => void;
};
