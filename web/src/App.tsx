import { LocalizationProvider } from '@mui/x-date-pickers'
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import { InfoProvider } from 'components/navbar/info/info-provider'
import { Navbar } from 'components/navbar/navbar'
import keycloak, { KEYCLOAK_TOKEN } from 'keycloak'
import { EnrichmentPage } from 'pages/enrichment/enrichment-page'
import { EnrichmentRequestDetail } from 'pages/enrichment/enrichment-request-detail'
import { EnrichmentRequestList } from 'pages/enrichment/enrichment-request-list'
import { ExportRequestDetailPage } from 'pages/export/export-request-detail'
import { ExportRequestListPage } from 'pages/export/export-request-list'
import { HomePage } from 'pages/home-page'
import { KrameriusJobInstanceDetailPage } from 'pages/job/kramerius-job-instance-detail-page'
import { NotFoundPage } from 'pages/not-found-page'
import { PublicationDetailPage } from 'pages/publication/publication-detail-page'
import { PublicationListPage } from 'pages/publication/publication-list-page'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'

const App = () => {
	return (
		<ReactKeycloakProvider
			authClient={keycloak}
			onTokens={tokens => {
				if (tokens.token) {
					localStorage.setItem(KEYCLOAK_TOKEN, tokens.token)
				}
			}}
		>
			<InfoProvider>
				<LocalizationProvider dateAdapter={AdapterDateFns}>
					<BrowserRouter>
						<Navbar />
						<Routes>
							<Route element={<HomePage />} path="/" />
							<Route path="publications">
								<Route element={<PublicationListPage />} index />
								<Route
									element={<PublicationDetailPage />}
									path=":publicationId"
								/>
							</Route>
							<Route path="jobs">
								<Route
									element={<KrameriusJobInstanceDetailPage />}
									path=":krameriusJobInstanceId"
								/>
							</Route>
							<Route path="enrichment">
								<Route element={<EnrichmentRequestList />} index />
								<Route element={<EnrichmentPage />} path="new" />
								<Route
									element={<EnrichmentRequestDetail />}
									path=":requestId"
								/>
							</Route>
							<Route path="exports">
								<Route element={<ExportRequestListPage />} index />
								{/* <Route element={<ExportRequestNew />} path="new" /> */}
								<Route
									element={<ExportRequestDetailPage />}
									path=":requestId"
								/>
							</Route>
							<Route element={<NotFoundPage />} path="*" />
						</Routes>
					</BrowserRouter>
				</LocalizationProvider>
				<ToastContainer newestOnTop position="bottom-left" />
			</InfoProvider>
		</ReactKeycloakProvider>
	)
}

export default App
