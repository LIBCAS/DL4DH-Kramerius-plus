import { createContext, useContext } from 'react'
import { User } from './user'

interface AuthContextInterface {
	auth: boolean
	setAuth: (auth: boolean) => void
	user: User | null
}

export const AuthContext = createContext<AuthContextInterface>({
	auth: false,
	setAuth: () => {},
	user: null,
})

export const useAuth = () => useContext(AuthContext)
