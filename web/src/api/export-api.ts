import { Export } from 'models/export'
import { QueryResults } from 'models/query-results'
import { customFetch } from 'utils/custom-fetch'

export const listExports = async (
	page: number,
	pageSize: number,
	publicationId?: string,
): Promise<QueryResults<Export>> => {
	let url = `/api/exports/list?page=${page}&pageSize=${pageSize}`

	url = publicationId ? `${url}&publicationId=${publicationId}` : url

	const response = await customFetch(url, {
		method: 'GET',
	})

	return await response?.json()
}
