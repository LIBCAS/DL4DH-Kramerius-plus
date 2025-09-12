import { EnrichmentRequest } from 'models/request/enrichment-request'
import { EnrichmentJobConfig } from 'models/job/config/enrichment-job-config'
import { QueryResults } from 'models/query-results'
import { EnrichmentRequestFilterDto } from 'pages/enrichment/enrichment-request-list'
import { customFetch } from 'utils/custom-fetch'

export const enrich = async (
	publicationIds: string[],
	configs: EnrichmentJobConfig[],
	name?: string,
): Promise<Response> => {
	const trimmedIds = publicationIds.map(id => id.trim()).filter(id => id !== '')
	return await customFetch('/api/enrichment/enrich', {
		method: 'POST',
		body: JSON.stringify({ publicationIds: trimmedIds, name, configs }),
	})
}

export const listEnrichmentRequests = async (
	page: number,
	pageSize: number,
	filter?: EnrichmentRequestFilterDto,
): Promise<QueryResults<EnrichmentRequest>> => {
	const filterCopy = { ...filter }

	const filterParams = filterCopy
		? Object.entries(filterCopy)
				.map(([key, value]) => (value ? `&${key}=${value}` : ''))
				.join('')
		: ''
	const url = `/api/enrichment/list?page=${page}&pageSize=${pageSize}${filterParams}`

	const response = await customFetch(url, {
		method: 'GET',
	})

	return await response.json()
}

export const getEnrichmentRequest = async (
	requestId: string,
): Promise<EnrichmentRequest> => {
	return await (
		await customFetch(`/api/enrichment/${requestId}`, {
			method: 'GET',
		})
	).json()
}

export const cancelRequest = async (requestId: string): Promise<Response> => {
	return await customFetch(`/api/enrichment/${requestId}/cancel`, {
		method: 'POST',
	})
}
