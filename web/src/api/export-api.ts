import {
	ExportFormat,
	ExportJobConfig,
} from 'components/publication/publication-export-dialog'
import { Export } from 'models/export'
import { ExportRequest } from 'models/export-request'
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

export const exportPublication = async (
	publicationIds: string[],
	config: ExportJobConfig,
	format: ExportFormat,
	name?: string,
): Promise<Response> => {
	return await customFetch(`/api/exports/${format}`, {
		method: 'POST',
		body: JSON.stringify({
			config,
			name,
			publicationIds,
		}),
	})
}

export const listExportRequests = async (): Promise<ExportRequest[]> => {
	const url = '/api/exports/list'

	const response = await customFetch(url, {
		method: 'GET',
	})

	return await response.json()
}

export const getExportRequest = async (
	requestId: string,
): Promise<ExportRequest> => {
	const url = `/api/exports/${requestId}`

	const response = await customFetch(url, {
		method: 'GET',
	})

	return await response.json()
}
