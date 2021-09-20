import { MuiPickersUtilsProvider } from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import { Switch, Route, BrowserRouter } from "react-router-dom";

import { Navbar } from "./components/NavBar";
import { Export } from "./modules/export/export";
import { Enrichment } from "./modules/enrichment/enrichment";
import { DialogProvider } from "./components/dialog/dialog-context-provider";

function App() {
  return (
    <DialogProvider>
      <MuiPickersUtilsProvider utils={DateFnsUtils}>
        <BrowserRouter>
          <Navbar />

          <Switch>
            <Route exact path="/export">
              <Export />
            </Route>
            <Route path="/">
              <Enrichment />
            </Route>
          </Switch>
        </BrowserRouter>
      </MuiPickersUtilsProvider>
    </DialogProvider>
  );
}

export default App;
