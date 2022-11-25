import { EnrichmentJobEventConfig } from './enrichment-job-event-config'

export interface TeiEnrichmentJobEventConfig extends EnrichmentJobEventConfig {
	publicationErrorTolerance: number
	pageErrorTolerance: number
}
