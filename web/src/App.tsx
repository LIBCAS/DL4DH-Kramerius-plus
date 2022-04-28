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
import { JobType } from 'models/job-type'

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
						<Route exact path="/publications/:publicationId?">
							<PublicationsPage />
						</Route>
						<Route exact path="/jobs/enriching/:jobId?">
							<JobPage jobType={JobType.Enriching} />
						</Route>
						<Route exact path="/jobs/exporting/:jobId?">
							<JobPage jobType={JobType.Exporting} />
						</Route>
						<Route exact path="/">
							<Enrichment />
						</Route>
						<Route render={() => 'Not found'} />
					</Switch>
				</BrowserRouter>
			</MuiPickersUtilsProvider>
			<ToastContainer newestOnTop position="bottom-left" />
		</DialogProvider>
	)
}

export default App
