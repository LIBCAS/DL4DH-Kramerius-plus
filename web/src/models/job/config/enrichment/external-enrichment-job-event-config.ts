import { MissingAltoStrategy } from 'enums/missing-alto-strategy'
import { EnrichmentJobEventConfig } from './enrichment-job-event-config'

export interface ExternalEnrichmentJobEventConfig
	extends EnrichmentJobEventConfig {
	missingAltoOption: MissingAltoStrategy
}
