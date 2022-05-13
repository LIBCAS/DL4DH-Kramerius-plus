import { KrameriusJob } from './kramerius-job'
import { MapType } from './map-type'

export interface JobEventConfig {
	parameters: MapType
	krameriusJob: KrameriusJob
}
