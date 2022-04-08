import { MuiPickersUtilsProvider } from '@material-ui/pickers'
import DateFnsUtils from '@date-io/date-fns'
import { Switch, Route, BrowserRouter } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'

import { Navbar } from './components/navbar/navbar'
import { Export } from './modules/export/export'
import { Enrichment } from './modules/enrichment/enrichment'
import { DialogProvider } from './components/dialog/dialog-context-provider'
import { JobPage } from 'modules/jobs/job-page'
import { PublicationsPage } from 'modules/publications/publications-page'

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
						<Route exact path="/publications">
							<PublicationsPage />
						</Route>
						<Route exact path="/jobs/enriching">
							<JobPage jobName="ENRICHING_JOB" />
						</Route>
						<Route exact path="/jobs/export">
							<JobPage jobName="EXPORTING_JOB" />
						</Route>
						<Route exact path="/">
							<Enrichment />
						</Route>
					</Switch>
				</BrowserRouter>
			</MuiPickersUtilsProvider>
			<ToastContainer newestOnTop position="bottom-left" />
		</DialogProvider>
	)
}

export default App
