import { EnrichmentJobEventConfig } from './config/enrichment-job-event-config'

export interface JobPlanCreate {
	name?: string
	publicationIds: string[]
	configs: EnrichmentJobEventConfig[]
}
