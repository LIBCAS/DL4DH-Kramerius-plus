import { JobConfig } from './job-event-config'

export interface EnrichmentJobConfig extends JobConfig {
	override: boolean
	pageErrorTolerance: number
}
