import React from "react";
import { AppBar, Toolbar, Typography } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import { Link } from "react-router-dom";

const useStyles = makeStyles((theme) => ({
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    marginRight: 40,
  },
}));

const NavBar = () => {
  const classes = useStyles();

  return (
    <AppBar>
      <Toolbar>
        <Typography variant="h6" className={classes.title}>
          Kramerius+ Client
        </Typography>
        <Button
          component={Link}
          to="/"
          color="inherit"
          className={classes.menuButton}
        >
          Obohacení
        </Button>
        <Button component={Link} to="/export" color="inherit">
          Export
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default NavBar;
