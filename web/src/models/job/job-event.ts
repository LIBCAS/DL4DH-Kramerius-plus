import { KrameriusJob } from 'enums/kramerius-job'
import { MapType } from 'models/map-type'
import { JobExecution } from './job-execution'

type JobEventConfig = {
	parameters: MapType
	krameriusJob: KrameriusJob
}

export interface JobEvent {
	id: string
	created: Date
	publicationId: string
	executions: JobExecution[]
	config: JobEventConfig
	lastExecutionStatus: string
	runErrorMessage?: string
	runErrorStackTrace?: string
}
