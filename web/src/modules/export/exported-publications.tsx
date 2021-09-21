import { useState } from "react";
import { v4 } from "uuid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import { FileRef } from "../../models";
import { ExportItem } from "../../components/export/export-item";
import { useInterval } from "../../hooks/use-interval";
import { getExportedPublications } from "./export-api";

const useStyles = makeStyles(() => ({
  title: {
    marginBottom: 10,
  },
  paper: {
    padding: "10px 24px",
    minHeight: 140,
    maxHeight: 640,
    overflow: "scroll",
  },
}));

export const ExportedPublications = () => {
  const classes = useStyles();
  const [exportedPublications, setExportedPublications] = useState<FileRef[]>(
    []
  );

  useInterval(
    async () => {
      const result = await getExportedPublications();

      if (result.length > 0) {
        setExportedPublications(result);
      }
    },
    5000,
    true
  );

  return (
    <Paper className={classes.paper}>
      <div className={classes.title}>
        <Typography variant="h6" color="primary">
          Vygenerované exporty
        </Typography>
      </div>
      <div>
        {exportedPublications.map((f) => (
          <ExportItem key={v4()} file={f} />
        ))}
      </div>
    </Paper>
  );
};
