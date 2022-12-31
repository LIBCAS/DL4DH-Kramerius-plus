import { KrameriusJob } from 'enums/kramerius-job'
import { DomainObject } from 'models/domain/domain-object'

export interface JobConfig extends DomainObject {
	jobType: KrameriusJob
}
