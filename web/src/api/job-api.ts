import { QueryResults } from 'models/query-results'
import { JobEvent } from 'models/job/job-event'
import { JobEventFilterDto } from '../modules/jobs/job-event/job-event-filter-dto'
import { JobType } from 'enums/job-type'
import { customFetch } from 'utils/custom-fetch'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'

export const listJobEvents = async (
	jobType: JobType,
	page: number,
	pageSize: number,
	filter?: JobEventFilterDto,
): Promise<QueryResults<JobEvent>> => {
	let url = `/api/jobs/${jobType}/list?page=${page}&pageSize=${pageSize}`
	url = filter?.publicationId
		? url + `&publicationId=${filter.publicationId}`
		: url
	url = filter?.jobType ? url + `&jobType=${filter.jobType}` : url
	url = filter?.lastExecutionStatus
		? url + `&lastExecutionStatus=${filter.lastExecutionStatus}`
		: url
	const response = await customFetch(url, {
		method: 'GET',
	})

	return await response?.json()
}

export const getJobEvent = async (jobEventId: string): Promise<JobEvent> => {
	const response = await customFetch(`/api/jobs/${jobEventId}`, {
		method: 'GET',
	})
	return await response?.json()
}

export const getJob = async (jobId: string): Promise<KrameriusJobInstance> => {
	const response = await customFetch(`/api/jobs/${jobId}`, {
		method: 'GET',
	})
	return await response?.json()
}

export const restartJob = async (jobEventId: string): Promise<Response> => {
	return await customFetch(`/api/jobs/${jobEventId}/restart`, {
		method: 'POST',
	})
}

export const stopJob = async (jobEventId: string): Promise<Response> => {
	return await customFetch(`/api/jobs/${jobEventId}/stop`, {
		method: 'POST',
	})
}
