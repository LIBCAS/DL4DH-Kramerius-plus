import { Input, Button, Paper, TextField, Box, Grid } from "@material-ui/core";
import { useState } from "react";
import { spacing } from '@material-ui/system';

function EnrichForm() {

    return (
          <Paper>
              <Grid>
            <h2>Enrich publication</h2>
            
            <TextField label={"Publication UUID"} />
            <Button >Enrich</Button>
            </Grid>
          </Paper>
    )
}

export default EnrichForm;