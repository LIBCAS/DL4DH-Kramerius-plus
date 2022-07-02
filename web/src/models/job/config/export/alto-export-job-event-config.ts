import { Params } from 'models'
import { ExportJobEventConfig } from './export-job-event-config'

export interface AltoExportJobEventConfig extends ExportJobEventConfig {
	params: Params
	delimiter: string
}
