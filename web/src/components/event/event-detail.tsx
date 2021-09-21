import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";

export type EventProps = {
  publicationTitle?: string;
  created?: string;
  started?: string;
  processing?: string;
  state?: string;
  took?: string;
  done?: string;
};

export const EventDetail = ({
  publicationTitle,
  processing,
  created,
  started,
  state,
  took,
  done,
}: EventProps) => {
  return (
    <Paper
      style={{
        width: "100%",
        padding: "10px 20px",
        marginBottom: 5,
      }}
    >
      <Grid container>
        <Grid item xs={4}>
          <Typography>Publikace:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{publicationTitle}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Status:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{state}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Vytvoření:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{created}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Spuštění:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{started}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Zpracovávání:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{processing}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Stav:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{done}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Trvanie:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{took}</Typography>
        </Grid>
      </Grid>
    </Paper>
  );
};
