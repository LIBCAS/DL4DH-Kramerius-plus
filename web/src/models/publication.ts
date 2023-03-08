import { ModsMetadata } from '.'
import { DigitalObjectModel } from '../enums/publication-model'
import { DomainObject } from './domain/domain-object'
import { PublishInfo } from './publish-info'

export interface Publication extends DomainObject {
	context: any
	created: Date
	title?: string
	collections?: string[]
	policy?: string
	modsMetadata?: ModsMetadata
	model?: DigitalObjectModel
	date?: string
	pages?: string[]
	index?: string
	volumeYear?: string
	periodicalItems?: string[]
	periodicalVolumes?: string[]
	issueNumber?: string
	partNumber?: string
	publishInfo: PublishInfo
	pageCount?: number
	rootTitle?: string
	rootId?: string
	paradata?: any
}
