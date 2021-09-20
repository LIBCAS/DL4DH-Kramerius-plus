import MuiDialog from "@material-ui/core/Dialog";

import { DialogProps } from "./types";

export function Dialog({
  opened,
  size,
  content,
  onClose,
  initialValues,
}: DialogProps & { onClose: () => void }) {
  return (
    <MuiDialog maxWidth={size} open={opened} onClose={onClose}>
      {content({ initialValues, onClose })}
    </MuiDialog>
  );
}
