import { JobEventConfig } from '../job-event-config'

export interface EnrichmentJobEventConfig extends JobEventConfig {
	override: boolean
}
