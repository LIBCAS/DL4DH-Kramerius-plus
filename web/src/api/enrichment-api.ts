import { EnrichmentRequest } from 'models/enrichment-request'
import { EnrichmentJobEventConfig } from 'models/job/config/enrichment/enrichment-job-event-config'
import { customFetch } from 'utils/custom-fetch'

export const enrich = async (
	publicationIds: string[],
	configs: EnrichmentJobEventConfig[],
	name?: string,
): Promise<Response> => {
	return await customFetch('/api/enrichment', {
		method: 'POST',
		body: JSON.stringify({ publicationIds, name, configs }),
	})
}

export const listEnrichmentRequests = async (): Promise<
	EnrichmentRequest[]
> => {
	const requestUrl = '/api/enrichment/list'

	const response = await customFetch(requestUrl, {
		method: 'GET',
	})

	return await response.json()
}

export const getEnrichmentRequest = async (
	requestId: string,
): Promise<EnrichmentRequest> => {
	const requestUrl = `/api/enrichment/${requestId}`

	const response = await customFetch(requestUrl, {
		method: 'GET',
	})

	return await response.json()
}
