import { EnrichmentJobEventConfig } from './enrichment-job-event-config'

export interface NdkEnrichmentJobEventConfig extends EnrichmentJobEventConfig {
	publicationErrorTolerance: number
	pageErrorTolerance: number
}
