import { JobConfig } from './job-config'

export interface EnrichmentJobConfig extends JobConfig {
	override: boolean
	pageErrorTolerance: number
}
