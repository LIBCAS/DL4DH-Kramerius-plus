import { useState, useCallback, useMemo } from "react";

import { DialogContextType, DialogProps } from "./types";

function DefaultDialogContent() {
  return <></>;
}

export function useDialog() {
  const [dialog, setDialog] = useState<DialogProps>({
    Content: DefaultDialogContent,
    opened: false,
    size: "sm",
  });

  /**
   * Opens dialog.
   */
  const open = useCallback<DialogContextType["open"]>((dialog) => {
    setDialog({ ...dialog, opened: true });
  }, []);

  const close = useCallback<DialogContextType["close"]>(() => {
    if (dialog?.opened) {
      setDialog({ ...dialog, opened: false });
    }
  }, [dialog]);

  const dialogCtx = useMemo(
    () => ({
      open,
      close,
    }),
    [open, close]
  );

  return {
    dialogCtx,
    dialog,
  };
}
