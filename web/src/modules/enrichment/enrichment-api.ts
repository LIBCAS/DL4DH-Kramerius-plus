import { ApiError } from 'models'
import { JobEventConfigCreateDto } from 'models/job-event-config-create-dto'
import { KrameriusJob } from 'models/kramerius-job'
import fetch from 'utils/fetch'

export async function createPlan(
	publicationIds: string[],
	configs: JobEventConfigCreateDto[],
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
		krameriusJob: KrameriusJob.ENRICHMENT_KRAMERIUS,
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
		krameriusJob: KrameriusJob.ENRICHMENT_EXTERNAL,
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
		krameriusJob: KrameriusJob.ENRICHMENT_NDK,
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
		krameriusJob: KrameriusJob.ENRICHMENT_TEI,
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
