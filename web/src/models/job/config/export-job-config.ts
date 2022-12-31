import { ExportFormat } from 'components/publication/publication-export-dialog'
import { Params } from 'models'
import { JobConfig } from './job-event-config'

export interface ExportJobConfig extends JobConfig {
	params: Params
	exportFormat: ExportFormat
}
