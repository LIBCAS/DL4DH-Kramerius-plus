import { Params } from 'models'
import { ExportKrameriusJob } from 'models/job/export-kramerius-job'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface TextExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	krameriusJob: ExportKrameriusJob.EXPORT_TEXT
	params: Params
	delimiter: string
}
