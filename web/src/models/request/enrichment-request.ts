import { EnrichmentJobEventConfig } from 'models/job/config/enrichment-job-event-config'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { EnrichmentRequestItem } from './enrichment-request-item'
import { Request } from './request'

export interface EnrichmentRequest extends Request {
	configs: EnrichmentJobEventConfig[]
	items: EnrichmentRequestItem[]
	createRequestJob: KrameriusJobInstance
}
