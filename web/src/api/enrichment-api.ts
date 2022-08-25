import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { EnrichmentJobEventConfig } from 'models/job/config/enrichment/enrichment-job-event-config'
import { customFetch } from 'utils/custom-fetch'

export const createPlan = async (
	publicationIds: string[],
	configs: EnrichmentJobEventConfig[],
	name?: string,
): Promise<Response> => {
	return await customFetch('/api/enrichment/plan', {
		method: 'POST',
		body: JSON.stringify({ publicationIds, name, configs }),
	})
}

export const downloadKStructure = async (
	publicationIds: string[],
	override: boolean,
): Promise<Response> => {
	const requestUrl = '/api/enrichment/kramerius'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS,
	}
	return await customFetch(requestUrl, {
		method: 'POST',
		body: JSON.stringify({ publicationIds, config, override }),
	})
}

export const enrichExternal = async (
	publicationIds: string[],
): Promise<Response> => {
	const requestUrl = '/api/enrichment/external'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
	}
	return await customFetch(requestUrl, {
		method: 'POST',
		body: JSON.stringify({ publicationIds, config }),
	})
}

export const enrichNdk = async (
	publicationIds: string[],
): Promise<Response> => {
	const requestUrl = '/api/enrichment/ndk'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_NDK,
	}
	return await customFetch(requestUrl, {
		method: 'POST',
		body: JSON.stringify({ publicationIds, config }),
	})
}

export const enrichTei = async (
	publicationIds: string[],
): Promise<Response> => {
	const requestUrl = '/api/enrichment/tei'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_TEI,
	}
	return await customFetch(requestUrl, {
		method: 'POST',
		body: JSON.stringify({ publicationIds, config }),
	})
}
