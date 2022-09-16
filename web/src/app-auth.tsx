import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns'
import { Routes, Route, BrowserRouter } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'

import { Navbar } from './components/navbar/navbar'
import { PublicationsPage } from 'pages/publication-list-page'
import { HomePage } from 'pages/home-page'
import { EnrichmentPage } from 'pages/enrichment/enrichment-page'
import { JobEventListPage } from 'pages/job-event-list-page'
import { JobEventDetailPage } from 'pages/job-event-detail-page'
import { NotFoundPage } from 'pages/not-found-page'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import keycloak from 'keycloak'
import { LocalizationProvider } from '@mui/x-date-pickers'
import { useAuth } from 'components/auth/auth-context'
import { FC } from 'react'
import { useInfo } from 'components/navbar/info/info-context'
import { EnrichmentRequestListPage } from 'pages/enrichment/enrichment-request-list-page'
import { EnrichmentRequestDetailPage } from 'pages/enrichment/enrichment-request-detail-page'
import { ExportRequestListPage } from 'pages/export/export-request-list-page'
import { ExportRequestDetailPage } from 'pages/export/export-request-detail-page'

export const AppAuth: FC = () => {
	const { setAuth } = useAuth()
	const { info } = useInfo()

	return (
		<ReactKeycloakProvider
			authClient={keycloak}
			onTokens={tokens => {
				if (tokens.token) {
					localStorage.setItem('token', tokens.token)
					setAuth(true)
				}
			}}
		>
			<LocalizationProvider dateAdapter={AdapterDateFns}>
				<BrowserRouter>
					<Navbar info={info} />
					<Routes>
						<Route element={<HomePage />} path="/" />
						<Route element={<PublicationsPage />} path="publications">
							<Route element={<PublicationsPage />} path=":publicationId" />
						</Route>
						<Route path="jobs/:jobType">
							<Route element={<JobEventListPage />} index />
							<Route element={<JobEventDetailPage />} path=":jobEventId" />
						</Route>
						<Route path="enrichment">
							<Route element={<EnrichmentRequestListPage />} index />
							<Route element={<EnrichmentPage />} path="new" />
							<Route
								element={<EnrichmentRequestDetailPage />}
								path=":requestId"
							/>
						</Route>
						<Route path="exports">
							<Route element={<ExportRequestListPage />} index />
							{/* <Route element={<ExportRequestNew />} path="new" /> */}
							<Route element={<ExportRequestDetailPage />} path=":requestId" />
						</Route>
						<Route element={<NotFoundPage />} path="*" />
					</Routes>
				</BrowserRouter>
			</LocalizationProvider>
			<ToastContainer newestOnTop position="bottom-left" />
		</ReactKeycloakProvider>
	)
}
