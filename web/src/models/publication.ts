import { ModsMetadata } from '.'
import { PublicationModel } from '../enums/publicationModel'

export interface Publication {
	context: any
	id: string
	title?: string
	collections?: string[]
	policy?: string
	modsMetadata?: ModsMetadata
	model?: PublicationModel
	date?: string
	pages?: string[]
	index?: string
	volumeYear?: string
	periodicalItems?: string[]
	periodicalVolumes?: string[]
	issueNumber?: string
	partNumber?: string
}
