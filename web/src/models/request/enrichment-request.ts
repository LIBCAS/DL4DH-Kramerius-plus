import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { EnrichmentRequestItem } from './enrichment-request-item'
import { Request } from './request'

export interface EnrichmentRequest extends Request {
	configs: any // TODO: EnrichmentJobConfigs
	items: EnrichmentRequestItem[]
	createRequestJob: KrameriusJobInstance
}
