import MuiDialog from "@material-ui/core/Dialog";

import { DialogProps } from "./types";

export function Dialog({
  opened,
  size,
  Content,
  onClose,
  initialValues,
}: DialogProps & { onClose: () => void }) {
  return (
    <MuiDialog maxWidth={size} open={opened} onClose={onClose}>
      <Content initialValues={initialValues} onClose={onClose} />
    </MuiDialog>
  );
}
