import { Publication } from 'models'
import { QueryResults } from 'models/query-results'
import { toast } from 'react-toastify'

export const getPublication = async (publicationId: string) => {
	try {
		const response = await fetch(`/api/publications/${publicationId}`, {
			method: 'GET',
			headers: new Headers({ 'Content-Type': 'application/json' }),
		})

		const json: Publication = await response.json()

		return json
	} catch (e) {
		toast(e as string)
	}
}

export const listPublications = async (page: number, pageSize: number) => {
	try {
		const response = await fetch(
			`/api/publications/list?page=${page}&pageSize=${pageSize}`,
			{
				method: 'GET',
				headers: new Headers({ 'Content-Type': 'application/json' }),
			},
		)

		const json: QueryResults<Publication> = await response.json()

		return json
	} catch (e) {
		toast(e as string)
	}
}
