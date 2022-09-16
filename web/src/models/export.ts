import { DatedObject } from './domain/dated-object'
import { FileRef } from './fileRef'
import { JobEvent } from './job/job-event'

export interface Export extends DatedObject {
	publicationId?: string
	publicationTitle?: string
	fileRef?: FileRef
	jobEvent: JobEvent
}
