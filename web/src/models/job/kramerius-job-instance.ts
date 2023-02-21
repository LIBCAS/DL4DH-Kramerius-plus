import { KrameriusJob } from 'enums/kramerius-job'
import { DomainObject } from 'models/domain/domain-object'
import { MapType } from 'models/map-type'
import { JobExecution } from './job-execution'
import { LastLaunch } from './job-launch'

export interface KrameriusJobInstance extends DomainObject {
	executionStatus: string
	executions: JobExecution[]
	jobType: KrameriusJob
	jobParameters: MapType
	lastLaunch: LastLaunch
}
