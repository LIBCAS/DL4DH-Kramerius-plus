import { DomainObject } from 'models/domain/domain-object'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { RequestState } from './request'

export interface EnrichmentChain extends DomainObject {
	publicationId: string
	publicationTitle: string
	model: string
	jobs: KrameriusJobInstance[]
	state: RequestState
}
