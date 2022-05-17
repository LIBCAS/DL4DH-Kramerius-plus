import { JobEvent } from 'models/job-event'
import { toast } from 'react-toastify'
import { JobType } from 'models/job-type'
import { QueryResults } from 'models/query-results'
import { KrameriusJob } from 'models/kramerius-job'

export const listJobEvents = async (
	jobType: JobType,
	page: number,
	pageSize: number,
	publicationId?: string,
	krameriusJob?: KrameriusJob,
) => {
	try {
		const jobTypeString = JobType[jobType].toLowerCase()
		let url = `/api/jobs/list/${jobTypeString}?page=${page}&pageSize=${pageSize}`
		url = publicationId ? url + `&publicationId=${publicationId}` : url
		url = krameriusJob ? url + `&jobType=${krameriusJob}` : url
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
