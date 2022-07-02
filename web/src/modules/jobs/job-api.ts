import { toast } from 'react-toastify'
import { QueryResults } from 'models/query-results'
import { JobEventFilterDto } from './job-event/job-event-filter-dto'
import { JobType } from 'enums/job-type'
import { JobEvent } from 'models/job/job-event'

export const listJobEvents = async (
	jobType: JobType,
	page: number,
	pageSize: number,
	filter?: JobEventFilterDto,
) => {
	try {
		let url = `/api/jobs/list/${jobType}?page=${page}&pageSize=${pageSize}`
		url = filter?.publicationId
			? url + `&publicationId=${filter.publicationId}`
			: url
		url = filter?.jobType ? url + `&jobType=${filter.jobType}` : url
		url = filter?.lastExecutionStatus
			? url + `&lastExecutionStatus=${filter.lastExecutionStatus}`
			: url
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
		const response = await fetch(`/api/jobs/${jobEventId}`, {
			method: 'GET',
		})

		const json: JobEvent = await response.json()

		return json
	} catch (e) {
		toast(e as string)
	}
}

export const restartJobExecution = async (jobEventId: string) => {
	try {
		await fetch(`/api/jobs/${jobEventId}/restart`, {
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
