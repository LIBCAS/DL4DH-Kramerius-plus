import { JobExecution } from 'models'
import { JobEventParameters } from './job-event-parameters'

export interface JobEvent {
	id: string
	created: Date
	jobName?: string
	publicationId: string
	parameters: JobEventParameters
	executions: JobExecution[]
}
