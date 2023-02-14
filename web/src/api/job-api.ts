import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { customFetch } from 'utils/custom-fetch'

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
