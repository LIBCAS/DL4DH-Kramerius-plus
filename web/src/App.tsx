import { AppAuth } from 'app-auth'
import { AuthProvider } from 'components/auth/auth-provider'
import { InfoProvider } from 'components/navbar/info/info-provider'

function App() {
	return (
		<AuthProvider>
			<InfoProvider>
				<AppAuth />
			</InfoProvider>
		</AuthProvider>
	)
}

export default App
