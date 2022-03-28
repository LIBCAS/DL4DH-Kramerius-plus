import { SingleEvent } from 'components/event/event-detail'
import { ApiError } from 'models'
import fetch from 'utils/fetch'

export async function enrich(publications: string[]) {
	const requestUrl = '/api/job/instance/enrich'

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
