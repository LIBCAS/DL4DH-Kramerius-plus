import Paper from "@material-ui/core/Paper";
import Grid from "@material-ui/core/Grid";
import Button from "@material-ui/core/Button";
import { makeStyles } from "@material-ui/core/styles";

import { FileRef } from "../../models";
import { ReadOnlyField } from "../read-only-field/read-only-field";

type Props = {
  file: FileRef;
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

export const ExportItem = ({ file }: Props) => {
  const classes = useStyles();

  const {
    id,
    publicationId,
    publicationTitle,
    created,
    name,
    contentType,
    size,
  } = file;

  const sizeMB = `${((size ?? 0) / 1048576).toFixed(2)} MB`;

  const localizedCreation = created
    ? new Date(created).toLocaleString("cs")
    : undefined;

  const handleDownloadExport = () => {
    window.open(
      process.env.PUBLIC_URL + `/api/export/download/${id}`,
      "_blank"
    );
  };

  return (
    <Paper className={classes.paper}>
      <Grid container justifyContent="center" alignItems="center">
        <Grid item xs={10}>
          <ReadOnlyField label="id:" value={id} />
          <ReadOnlyField label="publicationId:" value={publicationId} />
          <ReadOnlyField label="publicationTitle:" value={publicationTitle} />
          <ReadOnlyField label="created:" value={localizedCreation} />
          <ReadOnlyField label="name:" value={name} />
          <ReadOnlyField label="contentType" value={contentType} />
          <ReadOnlyField label="size:" value={sizeMB} />
        </Grid>
        <Grid item xs={2}>
          <Button
            variant="contained"
            color="primary"
            className={classes.exportButton}
            onClick={handleDownloadExport}
          >
            Stáhnout
          </Button>
        </Grid>
      </Grid>
    </Paper>
  );
};
