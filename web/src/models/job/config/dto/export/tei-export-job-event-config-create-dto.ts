import { TeiParams } from 'models'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface TeiExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	params: TeiParams
}
