import { KrameriusJob } from 'enums/kramerius-job'
import { DomainObject } from 'models/domain/domain-object'
import { MapType } from 'models/map-type'
import { JobExecution } from './job-execution'

export interface KrameriusJobInstance extends DomainObject {
	executionStatus: string
	executions: JobExecution[]
	jobType: KrameriusJob
	jobParameters: MapType
}
