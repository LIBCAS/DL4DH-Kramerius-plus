import { MuiPickersUtilsProvider } from '@material-ui/pickers'
import DateFnsUtils from '@date-io/date-fns'
import { Switch, Route, BrowserRouter } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'

import { Navbar } from './components/navbar/navbar'
import { DialogProvider } from './components/dialog/dialog-context-provider'
import { JobPage } from 'pages/job-event'
import { PublicationsPage } from 'pages/publication'
import { ExportList } from 'modules/export/export'
import { JobEventDetail } from 'modules/jobs/job-event/job-event-detail'
import { JobType } from 'enums/job-type'
import { Enrichment } from 'pages/enrichment'

function App() {
	return (
		<DialogProvider>
			<MuiPickersUtilsProvider utils={DateFnsUtils}>
				<BrowserRouter>
					<Navbar />
					<Switch>
						<Route exact path="/exports">
							<ExportList />
						</Route>
						<Route exact path="/publications/:publicationId?">
							<PublicationsPage />
						</Route>
						<Route exact path="/jobs/enriching">
							<JobPage jobType={JobType.Enriching} />
						</Route>
						<Route exact path="/jobs/enriching/:jobId?">
							<JobEventDetail />
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
