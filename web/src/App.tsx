import { Container, CssBaseline, makeStyles } from '@material-ui/core';
import { MuiPickersUtilsProvider } from '@material-ui/pickers';
import DateFnsUtils from '@date-io/date-fns';
import "./App.css";
import {
    BrowserRouter as Router,
    Switch,
    Route,
    BrowserRouter
  } from "react-router-dom";
import EnrichForm from './EnrichForm';
import Navbar from './components/NavBar';

const useStyles = makeStyles(() => ({
    root: {
      margin: `theme.spacing(3) auto`
    }
  }));
  
  function App() {
      const classes = useStyles();
  
    return (
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
        <Container className={classes.root}>
          <CssBaseline/>
          <BrowserRouter>
            <Navbar/>
          </BrowserRouter>
          <Router>
            <Switch>
              <Route path="/enrichment">
                <EnrichForm/>
              </Route>
            </Switch>
          </Router>
          </Container>
      </MuiPickersUtilsProvider>
    );
  }
  
  export default App;
  
