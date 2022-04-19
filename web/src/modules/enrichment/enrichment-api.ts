import { SingleEvent } from 'components/event/event-detail'
import { ApiError } from 'models'
import { JobEventCreateDto } from 'models/job-event-create-dto'
import fetch from 'utils/fetch'

export async function downloadKStructure(
	publicationIds: string[],
	override: boolean,
) {
	const requestUrl = '/api/enrich/download-k-structure'
	const publications: JobEventCreateDto[] = publicationIds.map(
		publicationId =>
			<JobEventCreateDto>{
				publicationId,
			},
	)

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publications, override }),
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
	const publications = mapToCreateDto(publicationIds)

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publications }),
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
	const publications = mapToCreateDto(publicationIds)

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publications }),
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
	const publications = mapToCreateDto(publicationIds)

	try {
		await fetch(requestUrl, {
			method: 'POST',
			headers: new Headers({ 'Content-Type': 'application/json' }),
			body: JSON.stringify({ publications }),
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

function mapToCreateDto(publicationIds: string[]): JobEventCreateDto[] {
	const publications: JobEventCreateDto[] = publicationIds.map(
		publicationId =>
			<JobEventCreateDto>{
				publicationId,
			},
	)

	return publications
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
