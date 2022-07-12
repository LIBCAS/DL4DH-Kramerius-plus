import { MuiPickersUtilsProvider } from '@material-ui/pickers'
import DateFnsUtils from '@date-io/date-fns'
import { Switch, Route, BrowserRouter } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'

import { Navbar } from './components/navbar/navbar'
import { DialogProvider } from './components/dialog/dialog-context-provider'
import { PublicationsPage } from 'pages/publication-list-page'
import { EnrichmentPage } from 'pages/enrichment-page'
import { ExportList } from 'modules/export/export'
import { JobEventListPage } from 'pages/job-event-list-page'
import { JobEventDetailPage } from 'pages/job-event-detail-page'

function App() {
	return (
		<DialogProvider>
			<MuiPickersUtilsProvider utils={DateFnsUtils}>
				<BrowserRouter>
					<Navbar />
					<Switch>
						<Route component={ExportList} exact path="/exports" />
						<Route
							component={PublicationsPage}
							exact
							path="/publications/:publicationId?"
						/>
						<Route component={JobEventListPage} exact path="/jobs/:jobType" />
						<Route
							component={JobEventDetailPage}
							exact
							path="/jobs/:jobType/:jobEventId"
						/>
						<Route component={EnrichmentPage} exact path="/" />
						<Route render={() => 'Not found'} />
					</Switch>
				</BrowserRouter>
			</MuiPickersUtilsProvider>
			<ToastContainer newestOnTop position="bottom-left" />
		</DialogProvider>
	)
}

export default App
