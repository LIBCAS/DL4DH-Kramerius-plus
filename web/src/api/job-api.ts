import { QueryResults } from 'models/query-results'
import { JobEvent } from 'models/job/job-event'
import { JobEventFilterDto } from '../modules/jobs/job-event/job-event-filter-dto'
import { JobType } from 'enums/job-type'
import { customFetch } from 'utils/custom-fetch'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'

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
