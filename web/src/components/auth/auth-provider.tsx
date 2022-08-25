import { getCurrentUser } from 'api/user-api'
import { FC, useEffect, useState } from 'react'
import { AuthContext } from './auth-context'
import { User } from './user'

export const AuthProvider: FC<{ children: JSX.Element }> = ({ children }) => {
	const [auth, setAuth] = useState<boolean>(false)
	const [user, setUser] = useState<User | null>(null)

	useEffect(() => {
		const isAuth = async () => {
			if (auth) {
				try {
					const currentUser = await getCurrentUser()

					setUser(currentUser)
				} catch (error) {
					setUser(null)
				}
			}
		}
		isAuth()
	}, [auth])

	return (
		<AuthContext.Provider value={{ auth, setAuth, user }}>
			{children}
		</AuthContext.Provider>
	)
}
