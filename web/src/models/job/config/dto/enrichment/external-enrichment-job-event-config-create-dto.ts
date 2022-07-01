import { MissingAltoStrategy } from 'enums/missing-alto-strategy'
import { EnrichmentJobEventConfigCreateDto } from '../enrichment-job-event-config-create-dto'

export interface ExternalEnrichmentJobEventConfigCreateDto
	extends EnrichmentJobEventConfigCreateDto {
	missingAltoOption: MissingAltoStrategy
}
