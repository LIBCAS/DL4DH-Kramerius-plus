import { Params } from 'models'
import { ExportJobEventConfig } from './export-job-event-config'

export interface TextExportJobEventConfig extends ExportJobEventConfig {
	params: Params
	delimiter: string
}
