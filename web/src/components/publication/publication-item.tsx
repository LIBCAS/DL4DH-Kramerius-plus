import { useContext } from "react";
import Paper from "@material-ui/core/Paper";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import { makeStyles } from "@material-ui/core/styles";

import { Publication } from "../../models";
import { ReadOnlyField } from "../read-only-field/read-only-field";
import { DialogContext } from "../dialog/dialog-context";
import { PublicationExportDialog } from "./publication-export-dialog";

type Props = {
  publication: Publication;
};

const useStyles = makeStyles(() => ({
  paper: {
    marginBottom: 8,
    padding: "8px 16px",
  },
  exportButton: {
    textTransform: "none",
    padding: "6px 10px",
  },
}));

export const PublicationItem = ({ publication }: Props) => {
  const classes = useStyles();
  const { open } = useContext(DialogContext);

  const {
    id,
    title,
    date,
    issueNumber,
    partNumber,
    index,
    pages = [],
    model,
    volumeYear,
  } = publication;

  const handleOpenExportDialog = () => {
    open({
      initialValues: {
        id,
      },
      content: PublicationExportDialog,
      size: "md",
    });
  };

  return (
    <Paper className={classes.paper}>
      <Grid container justifyContent="center" alignItems="center">
        <Grid item xs={10}>
          <ReadOnlyField label="id:" value={id} />
          <ReadOnlyField label="title:" value={title} />
          <ReadOnlyField label="date:" value={date} />
          <ReadOnlyField label="issueNumber:" value={issueNumber} />
          <ReadOnlyField label="partNumber:" value={partNumber} />
          <ReadOnlyField label="index:" value={index} />
          <ReadOnlyField label="pages:" value={pages.join(", ")} />
          <ReadOnlyField label="model:" value={model} />
          <ReadOnlyField label="volumeYear:" value={volumeYear} />
        </Grid>
        <Grid item xs={2}>
          <Button
            variant="contained"
            color="primary"
            className={classes.exportButton}
            onClick={handleOpenExportDialog}
          >
            Exportovat
          </Button>
        </Grid>
      </Grid>
    </Paper>
  );
};
