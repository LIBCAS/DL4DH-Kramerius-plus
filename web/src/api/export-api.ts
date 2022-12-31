import {
	ExportFormat,
	ExportJobConfig,
} from 'components/publication/publication-export-dialog'
import { ExportRequest } from 'models/request/export-request'
import { QueryResults } from 'models/query-results'
import { customFetch } from 'utils/custom-fetch'
import { ExportRequestFilterDto } from 'pages/export/export-request-list'

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

export const listExportRequests = async (
	page: number,
	pageSize: number,
	filter?: ExportRequestFilterDto,
): Promise<QueryResults<ExportRequest>> => {
	const filterCopy = { ...filter }

	const filterParams = filterCopy
		? Object.entries(filterCopy)
				.map(([key, value]) => (value ? `&${key}=${value}` : ''))
				.join('')
		: ''
	const url = `/api/exports/list?page=${page}&pageSize=${pageSize}${filterParams}`

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
