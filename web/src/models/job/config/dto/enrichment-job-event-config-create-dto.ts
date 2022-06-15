import { EnrichmentKrameriusJob } from 'models/job/enrichment-kramerius-job'
import { JobEventConfigCreateDto } from './job-event-config-create-dto'

export interface EnrichmentJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	krameriusJob: EnrichmentKrameriusJob
	override: boolean
}
