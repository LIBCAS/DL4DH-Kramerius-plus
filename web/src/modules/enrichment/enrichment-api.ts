import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { ApiError } from 'models'
import { EnrichmentJobEventConfigCreateDto } from 'models/job/config/dto/enrichment-job-event-config-create-dto'
import fetch from 'utils/fetch'

export async function createPlan(
	publicationIds: string[],
	configs: EnrichmentJobEventConfigCreateDto[],
	name?: string,
) {
	try {
		await fetch('/api/enrichment/plan', {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publicationIds, name, configs }),
		})

		return {
			ok: true,
			data: {},
		}
	} catch (e) {
		console.error(e)

		return {
			ok: false,
			data: e as ApiError,
		}
	}
}

export async function downloadKStructure(
	publicationIds: string[],
	override: boolean,
) {
	const requestUrl = '/api/enrichment/kramerius'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_KRAMERIUS,
	}

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publicationIds, config, override }),
		})

		return {
			ok: true,
			data: {},
		}
	} catch (e) {
		console.error(e)

		return {
			ok: false,
			data: e as ApiError,
		}
	}
}

export async function enrichExternal(publicationIds: string[]) {
	const requestUrl = '/api/enrichment/external'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL,
	}

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publicationIds, config }),
		})

		return {
			ok: true,
			data: {},
		}
	} catch (e) {
		console.error(e)

		return {
			ok: false,
			data: e as ApiError,
		}
	}
}

export async function enrichNdk(publicationIds: string[]) {
	const requestUrl = '/api/enrichment/ndk'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_NDK,
	}

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publicationIds, config }),
		})

		return {
			ok: true,
			data: {},
		}
	} catch (e) {
		console.error(e)

		return {
			ok: false,
			data: e as ApiError,
		}
	}
}

export async function enrichTei(publicationIds: string[]) {
	const requestUrl = '/api/enrichment/tei'
	const config = {
		krameriusJob: EnrichmentKrameriusJob.ENRICHMENT_TEI,
	}

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publicationIds, config }),
		})

		return {
			ok: true,
			data: {},
		}
	} catch (e) {
		console.error(e)

		return {
			ok: false,
			data: e as ApiError,
		}
	}
}
