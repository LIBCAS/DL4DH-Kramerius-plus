import { useState } from "react";
import { v4 } from "uuid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";

import { PublicationItem } from "../../components/publication/publication-item";
import { useInterval } from "../../hooks/use-interval";
import { Publication } from "../../models";
import { getPublications } from "./export-api";

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

export const PublicationsForExport = () => {
  const classes = useStyles();
  const [publications, setPublications] = useState<Publication[]>([]);

  useInterval(
    async () => {
      const result = await getPublications();

      if (result.length > 0) {
        setPublications(result);
      }
    },
    5000,
    true
  );

  return (
    <Paper className={classes.paper}>
      <div className={classes.title}>
        <Typography variant="h6" color="primary">
          Obohacené publikace
        </Typography>
      </div>
      <div>
        {publications.map((p) => (
          <PublicationItem key={v4()} publication={p} />
        ))}
      </div>
    </Paper>
  );
};
