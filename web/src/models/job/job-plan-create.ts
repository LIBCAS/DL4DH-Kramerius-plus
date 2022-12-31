import { EnrichmentJobConfig } from './config/enrichment-job-config'

export interface JobPlanCreate {
	name?: string
	publicationIds: string[]
	configs: EnrichmentJobConfig[]
}
