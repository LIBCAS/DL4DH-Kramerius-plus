import { EnrichmentJobConfig } from 'models/job/config/enrichment-job-config'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { EnrichmentRequestItem } from './enrichment-request-item'
import { Request } from './request'

export interface EnrichmentRequest extends Request {
	configs: EnrichmentJobConfig[]
	items: EnrichmentRequestItem[]
	createRequestJob: KrameriusJobInstance
}
