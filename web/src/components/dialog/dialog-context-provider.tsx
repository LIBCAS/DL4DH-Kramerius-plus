import { ReactNode } from "react";

import { Dialog } from "./dialog";
import { DialogContext } from "./dialog-context";
import { useDialog } from "./dialog-context-hook";

export function DialogProvider({ children }: { children: ReactNode }) {
  const { dialogCtx, dialog } = useDialog();

  return (
    <DialogContext.Provider value={dialogCtx}>
      {children}

      <Dialog
        onClose={dialogCtx.close}
        opened={dialog.opened}
        Content={dialog.Content}
        size={dialog.size}
        initialValues={dialog.initialValues}
      />
    </DialogContext.Provider>
  );
}
