import { AppBar, IconButton, Toolbar, Typography } from "@material-ui/core";
import { withStyles } from "@material-ui/styles";
import { Button } from "bootstrap";

function Navigation(styles) {
    return (
        <AppBar position='static'>
            <Toolbar>
                <IconButton
                    edge='start'
                    className={classes.menuButton}
                    color='inherit'
                    aria-label='menu'
                >
                    <MenuIcon />
                </IconButton>
                <Typography variant='h6' className={classes.title}>
                    News
                </Typography>
                <Button color='inherit'>Login</Button>
            </Toolbar>
        </AppBar>
    );
}

export default withStyles(styles)(Navigation);