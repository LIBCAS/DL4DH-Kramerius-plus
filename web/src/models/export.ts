import { DatedObject } from './domain/dated-object'
import { FileRef } from './fileRef'
import { KrameriusJobInstance } from './job/kramerius-job-instance'

export interface Export extends DatedObject {
	publicationId?: string
	publicationTitle?: string
	model?: string
	fileRef?: FileRef
	exportJob?: KrameriusJobInstance
	children: Export[]
}
