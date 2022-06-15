import { Params } from 'models'
import { ExportKrameriusJob } from 'models/job/export-kramerius-job'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface AltoExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	krameriusJob: ExportKrameriusJob.EXPORT_ALTO
	params: Params
	delimiter: string
}
