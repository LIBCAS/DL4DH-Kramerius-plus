import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";

export type EventProps = {
  publication?: string;
  created?: string;
  processing?: string;
};

export const EventDetail = ({
  publication,
  processing,
  created,
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
          <Typography color="primary">{publication}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Vytvoření:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{created}</Typography>
        </Grid>
        <Grid item xs={4}>
          <Typography>Zpracovávání:</Typography>
        </Grid>
        <Grid item xs={8}>
          <Typography color="primary">{processing}</Typography>
        </Grid>
      </Grid>
    </Paper>
  );
};
