import { User } from 'models/user'
import { customFetch } from 'utils/custom-fetch'

export const getCurrentUser = async (): Promise<User> => {
	const response = await customFetch('/api/users/me', {
		method: 'GET',
	})

	return response.json()
}
