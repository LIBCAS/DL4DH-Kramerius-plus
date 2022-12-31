import { ExportJobConfig } from './export-job-config'

export interface ExportCsvJobConfig extends ExportJobConfig {
	delimiter: string
}
