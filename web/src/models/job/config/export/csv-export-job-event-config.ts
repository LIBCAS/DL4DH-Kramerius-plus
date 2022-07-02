import { Params } from 'models'
import { ExportJobEventConfig } from './export-job-event-config'

export interface CsvExportJobEventConfig extends ExportJobEventConfig {
	params: Params
	delimiter: string
}
