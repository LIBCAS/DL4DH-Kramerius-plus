import { JobExecution } from 'models'
import { JobEventConfig } from './job-event-config'

export interface JobEvent {
	id: string
	created: Date
	jobName?: string
	publicationId: string
	executions: JobExecution[]
	config: JobEventConfig
	lastExecutionStatus: string
}
