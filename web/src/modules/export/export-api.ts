import { Export } from 'models'
import { QueryResults } from 'models/query-results'
import fetch from 'utils/fetch'

export const listExports = async (
	page: number,
	pageSize: number,
	publicationId?: string,
) => {
	try {
		let url = `/api/exports/list?page=${page}&pageSize=${pageSize}`

		url = publicationId ? `${url}&publicationId=${publicationId}` : url

		const response = await fetch(url, {
			method: 'GET',
			headers: new Headers({ 'Content-Type': 'application/json' }),
		})

		const json: QueryResults<Export> = await response.json()

		return json
	} catch (e) {
		console.log(e)
	}
}
