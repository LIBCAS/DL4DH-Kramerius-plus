import { Publication } from 'models'
import { toast } from 'react-toastify'

export const getPublication = async (publicationId: string) => {
	try {
		const response = await fetch(`/api/publication/${publicationId}`, {
			method: 'GET',
			headers: new Headers({ 'Content-Type': 'application/json' }),
		})

		const json: Publication = await response.json()

		return json
	} catch (e) {
		toast(e as string)
	}
}

export const listPublications = async () => {
	try {
		const response = await fetch('/api/publication/list', {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
		})

		const json: Publication[] = await response.json()

		return json
	} catch (e) {
		return []
	}
}
