import { JobExecution } from 'models/job-execution'
import { JobInstance } from 'models/job-instance'

export const getJobNames = async () => {
	try {
		const response = await fetch('/api/job/list', {
			method: 'GET',
		})

		const json: string[] = await response.json()

		return json
	} catch (e) {
		return []
	}
}

export const getJobInstances = async (jobName: string) => {
	try {
		const response = await fetch(`/api/job/instance/${jobName}/list`, {
			method: 'GET',
		})

		const json: JobInstance[] = await response.json()

		return json
	} catch (e) {
		return []
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

export const restartJobExecution = async (executionId: number) => {
	try {
		await fetch(`/api/job/execution/${executionId}/restart`, {
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
