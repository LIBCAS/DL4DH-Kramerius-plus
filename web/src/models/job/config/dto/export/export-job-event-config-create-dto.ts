import { AltoExportJobEventConfigCreateDto } from './alto-export-job-event-config-create-dto'
import { CsvExportJobEventConfigCreateDto } from './csv-export-job-event-config-create-dto'
import { JsonExportJobEventConfigCreateDto } from './json-export-job-event-config-create-dto'
import { TeiExportJobEventConfigCreateDto } from './tei-export-job-event-config-create-dto'

export type ExportJobEventConfigCreateDto =
	| CsvExportJobEventConfigCreateDto
	| JsonExportJobEventConfigCreateDto
	| TeiExportJobEventConfigCreateDto
	| AltoExportJobEventConfigCreateDto
