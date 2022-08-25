import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns'
import { Routes, Route, BrowserRouter } from 'react-router-dom'
import { ToastContainer } from 'react-toastify'

import { Navbar } from './components/navbar/navbar'
import { PublicationsPage } from 'pages/publication-list-page'
import { HomePage } from 'pages/home-page'
import { EnrichmentPage } from 'pages/enrichment-page'
import { ExportListPage } from 'pages/export-list-page'
import { JobEventListPage } from 'pages/job-event-list-page'
import { JobEventDetailPage } from 'pages/job-event-detail-page'
import { NotFoundPage } from 'pages/not-found-page'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import keycloak from 'keycloak'
import { LocalizationProvider } from '@mui/x-date-pickers'
import { useAuth } from 'components/auth/auth-context'
import { FC } from 'react'
import { useInfo } from 'components/navbar/info/info-context'

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
						<Route element={<ExportListPage />} path="exports" />
						<Route element={<PublicationsPage />} path="publications">
							<Route element={<PublicationsPage />} path=":publicationId" />
						</Route>
						<Route path="jobs/:jobType">
							<Route element={<JobEventListPage />} index />
							<Route element={<JobEventDetailPage />} path=":jobEventId" />
						</Route>
						<Route element={<EnrichmentPage />} path="enrichment" />
						<Route element={<NotFoundPage />} path="*" />
					</Routes>
				</BrowserRouter>
			</LocalizationProvider>
			<ToastContainer newestOnTop position="bottom-left" />
		</ReactKeycloakProvider>
	)
}
