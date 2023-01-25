import { useKeycloak } from '@react-keycloak/web'
import { FC } from 'react'
import { Navigate } from 'react-router-dom'

export const PrivateRoute: FC<{ children: JSX.Element }> = ({ children }) => {
	const { keycloak } = useKeycloak()

	return keycloak.authenticated ? children : <Navigate to={'/'} />
}
