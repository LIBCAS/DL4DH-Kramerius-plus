import { JobExecution } from 'models/job-execution'
import { JobEvent } from 'models/job-event'
import { QueryResult } from 'models/query-results'
import { toast } from 'react-toastify'

export const listJobEvents = async (jobName: string) => {
	try {
		const response = await fetch(`/api/job/list`, {
			method: 'POST',
			headers: {
				Accept: 'application/json',
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				filters: [
					{
						field: 'krameriusJob',
						operator: 'EQ',
						value: jobName,
					},
				],
			}),
		})

		const json: QueryResult<JobEvent> = await response.json()

		console.log(json)

		return json
	} catch (e) {
		return undefined
	}
}

export const getJobEvent = async (jobEventId: string) => {
	try {
		const response = await fetch(`/api/job/${jobEventId}`, {
			method: 'GET',
		})

		const json: JobEvent = await response.json()

		console.log(json)

		return json
	} catch (e) {
		toast(e as string)
	}
}

export const getJobExecutions = async (instanceId: string) => {
	try {
		const response = await fetch(`/api/job/instance/${instanceId}/executions`, {
			method: 'GET',
		})

		const json: JobExecution[] = await response.json()

		return json
	} catch (e) {
		return []
	}
}

export const getJobExecution = async (executionId: string) => {
	try {
		const response = await fetch(`/api/job/execution/${executionId}`, {
			method: 'GET',
		})

		const json: JobExecution = await response.json()

		return json
	} catch (e) {
		return []
	}
}

export const restartJobExecution = async (jobEventId: string) => {
	try {
		await fetch(`/api/job/${jobEventId}/restart`, {
			method: 'POST',
		})

		return {
			ok: true,
			data: {},
		}
	} catch (e) {
		return {
			ok: false,
			data: {},
		}
	}
}
