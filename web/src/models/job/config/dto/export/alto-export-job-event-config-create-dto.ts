import { Params } from 'models'
import { JobEventConfigCreateDto } from '../job-event-config-create-dto'

export interface AltoExportJobEventConfigCreateDto
	extends JobEventConfigCreateDto {
	params: Params
	delimiter: string
}
