import { EnrichmentJobEventConfigCreateDto } from '../enrichment-job-event-config-create-dto'
import { ExternalEnrichmentJobEventConfigCreateDto } from './external-enrichment-job-event-config-create-dto'

export type EnrichmentJobEventConfigType =
	| EnrichmentJobEventConfigCreateDto
	| ExternalEnrichmentJobEventConfigCreateDto
