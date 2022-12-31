import { TeiParams } from 'models'
import { ExportJobConfig } from './export-job-config'

export interface ExportTeiJobConfig extends ExportJobConfig {
	teiParams: TeiParams
}
