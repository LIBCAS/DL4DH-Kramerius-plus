import { MuiPickersUtilsProvider } from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import { Switch, Route, BrowserRouter } from "react-router-dom";

import Navbar from "./components/NavBar";
import { Enrichment } from "./modules/enrichment/enrichment";

function App() {
  return (
    <MuiPickersUtilsProvider utils={DateFnsUtils}>
      <BrowserRouter>
        <Navbar />

        <Switch>
          <Route exact path="/export">
            Export
          </Route>
          <Route path="/">
            <Enrichment />
          </Route>
        </Switch>
      </BrowserRouter>
    </MuiPickersUtilsProvider>
  );
}

export default App;
