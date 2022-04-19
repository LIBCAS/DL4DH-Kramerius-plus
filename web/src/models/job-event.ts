import { JobExecution } from 'models'
import { KrameriusJob } from './kramerius-job'
import { MapType } from './map-type'

export interface JobEvent {
	id: string
	created: Date
	jobName?: string
	publicationId: string
	parameters: MapType
	executions: JobExecution[]
	krameriusJob: KrameriusJob
	lastExecutionStatus: string
}
