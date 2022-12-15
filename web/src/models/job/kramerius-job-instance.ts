import { KrameriusJob } from 'enums/kramerius-job'
import { DomainObject } from 'models/domain/domain-object'
import { JobExecution } from './job-execution'

export interface KrameriusJobInstance extends DomainObject {
	jobStatus: string
	executions: JobExecution[]
	jobType: KrameriusJob
}
