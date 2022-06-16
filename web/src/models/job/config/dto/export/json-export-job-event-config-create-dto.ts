import { Params } from 'models'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface JsonExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	params: Params
}
