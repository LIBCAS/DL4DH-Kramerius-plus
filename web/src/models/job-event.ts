import { JobExecution } from 'models'
import { MapType } from './map-type'

export interface JobEvent {
	id: string
	created: Date
	jobName?: string
	publicationId: string
	parameters: MapType
	executions: JobExecution[]
}
