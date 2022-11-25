import { EnrichmentJobEventConfig } from './enrichment-job-event-config'

export interface ExternalEnrichmentJobEventConfig
	extends EnrichmentJobEventConfig {
	publicationErrorTolerance: number
	pageErrorTolerance: number
}
