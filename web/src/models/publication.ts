import { ModsMetadata } from '.'
import { DigitalObjectModel } from '../enums/publication-model'
import { PublishInfo } from './publish-info'

export interface Publication {
	context: any
	id: string
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
}
