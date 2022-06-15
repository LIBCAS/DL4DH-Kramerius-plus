import { TeiParams } from 'models'
import { ExportKrameriusJob } from 'models/job/export-kramerius-job'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface TeiExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	krameriusJob: ExportKrameriusJob.EXPORT_TEI
	params: TeiParams
}
