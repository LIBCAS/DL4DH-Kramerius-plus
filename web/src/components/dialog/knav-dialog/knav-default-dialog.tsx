import { ReactNode, useContext } from "react";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";
import { makeStyles } from "@material-ui/core/styles";

import { DialogContext } from "../dialog-context";

interface Props {
  title: string;
  actions: ReactNode;
  children: ReactNode;
  closeBtnLabel?: string;
}

const useStyles = makeStyles(() => ({
  root: {
    minWidth: 300,
  },
  title: {
    display: "flex",
    alignItems: "center",
    height: 30,
    backgroundColor: "#F0F3F4",
    "& > h2": {
      fontSize: 16,
    },
  },
  content: {
    "& *": {
      fontSize: 16,
    },
  },
  actions: {},
}));

export function DefaultDialog({
  title,
  actions,
  children,
  closeBtnLabel = "Zavřít",
}: Props) {
  const { close } = useContext(DialogContext);
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <DialogTitle className={classes.title}>{title}</DialogTitle>
      <DialogContent className={classes.content}>{children}</DialogContent>
      <DialogActions className={classes.actions}>
        {actions}
        <Button key="close" onClick={close}>
          {closeBtnLabel}
        </Button>
      </DialogActions>
    </div>
  );
}
