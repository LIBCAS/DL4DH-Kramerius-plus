import { KrameriusJob } from 'enums/kramerius-job'
import { DomainObject } from 'models/domain/domain-object'

export interface JobEventConfig extends DomainObject {
	jobType: KrameriusJob
}
