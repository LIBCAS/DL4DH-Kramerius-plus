import { TeiParams } from 'models'
import { ExportJobEventConfig } from './export-job-event-config'

export interface TeiExportJobEventConfig extends ExportJobEventConfig {
	params: TeiParams
}
