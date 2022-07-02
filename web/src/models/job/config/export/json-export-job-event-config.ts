import { Params } from 'models'
import { ExportJobEventConfig } from './export-job-event-config'

export interface JsonExportJobEventConfig extends ExportJobEventConfig {
	params: Params
}
