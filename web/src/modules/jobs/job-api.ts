import { JobExecution } from 'models/job-execution'
import { JobEvent } from 'models/job-event'
import { toast } from 'react-toastify'
import { JobType } from 'models/job-type'
import { QueryResults } from 'models/query-results'

export const listJobEvents = async (
	jobType: JobType,
	page: number,
	pageSize: number,
	publicationId?: string,
) => {
	try {
		const jobTypeString = JobType[jobType].toLowerCase()
		const baseUrl = `/api/job/list/${jobTypeString}?page=${page}&pageSize=${pageSize}`
		const url = publicationId
			? baseUrl + `&publicationId=${publicationId}`
			: baseUrl
		const response = await fetch(url, {
			method: 'GET',
			headers: {
				Accept: 'application/json',
				'Content-Type': 'application/json',
			},
		})

		const json: QueryResults<JobEvent> = await response.json()

		return json
	} catch (e) {
		toast(e as string)
	}
}

export const getJobEvent = async (jobEventId: string) => {
	try {
		const response = await fetch(`/api/job/${jobEventId}`, {
			method: 'GET',
		})

		const json: JobEvent = await response.json()

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
