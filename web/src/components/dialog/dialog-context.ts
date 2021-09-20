import { createContext } from 'react';

import { DialogContextType } from './types';

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const DialogContext = createContext<DialogContextType>(undefined as any);
