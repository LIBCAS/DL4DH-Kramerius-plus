import { SingleEvent } from 'components/event/event-detail'
import { ApiError } from 'models'
import { JobEventConfigCreateDto } from 'models/job-event-config-create-dto'
import { KrameriusJob } from 'models/kramerius-job'
import fetch from 'utils/fetch'

export async function createPlan(
	publicationIds: string[],
	configs: JobEventConfigCreateDto[],
) {
	try {
		await fetch('/api/enrich/plan', {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publicationIds, configs }),
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
	const requestUrl = '/api/enrich/download-k-structure'
	const config = {
		krameriusJob: KrameriusJob.DOWNLOAD_K_STRUCTURE,
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
	const requestUrl = '/api/enrich/enrich-external'
	const config = {
		krameriusJob: KrameriusJob.ENRICH_EXTERNAL,
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
	const requestUrl = '/api/enrich/enrich-ndk'
	const config = {
		krameriusJob: KrameriusJob.ENRICH_NDK,
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
	const requestUrl = '/api/enrich/enrich-tei'
	const config = {
		krameriusJob: KrameriusJob.ENRICH_TEI,
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

export async function getRunningTasks(): Promise<SingleEvent[]> {
	try {
		const response = await fetch('/api/job/list', {
			method: 'GET',
		})

		const json: SingleEvent[] = await response.json()

		return json
	} catch (e) {
		return []
	}
}

export async function getFinishedTasks(): Promise<SingleEvent[]> {
	try {
		const response = await fetch('/api/scheduler/tasks/finished', {
			method: 'GET',
		})

		const json: SingleEvent[] = await response.json()

		return json
	} catch (e) {
		return []
	}
}

export async function cancelTask(id: string) {
	try {
		const response = await fetch(`/api/scheduler/tasks/cancel/${id}`, {
			method: 'POST',
		})
		return {
			ok: response.ok,
		}
	} catch (e) {
		console.error(e)

		return {
			ok: false,
		}
	}
}
