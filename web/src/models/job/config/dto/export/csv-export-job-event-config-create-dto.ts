import { Params } from 'models'
import { ExportKrameriusJob } from 'models/job/export-kramerius-job'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface CsvExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	krameriusJob: ExportKrameriusJob.EXPORT_CSV
	params: Params
	delimiter: string
}
