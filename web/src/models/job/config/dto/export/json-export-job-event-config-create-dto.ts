import { Params } from 'models'
import { ExportKrameriusJob } from 'models/job/export-kramerius-job'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface JsonExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	krameriusJob: ExportKrameriusJob.EXPORT_JSON
	params: Params
}
