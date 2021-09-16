import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { MuiPickersUtilsProvider } from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";

import Navbar from "./components/NavBar";
import { Enrichment } from "./modules/enrichment";

function App() {
  return (
    <MuiPickersUtilsProvider utils={DateFnsUtils}>
      <Router>
        <Navbar />

        <Switch>
          <Route exact path="/enrichment">
            <Enrichment />
          </Route>
          <Route exact path="/export">
            Export
          </Route>
        </Switch>
      </Router>
    </MuiPickersUtilsProvider>
  );
}

export default App;
