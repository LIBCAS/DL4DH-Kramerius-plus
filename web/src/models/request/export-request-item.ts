import { Export } from 'models'
import { DomainObject } from 'models/domain/domain-object'

export interface ExportRequestItem extends DomainObject {
	publicationId: string
	publicationTitle: string
	model: string
	order: number
	rootExport?: Export
}
