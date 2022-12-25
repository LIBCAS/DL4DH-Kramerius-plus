import { Export, FileRef } from 'models'
import { DatedObject } from './domain/dated-object'
import { KrameriusJobInstance } from './job/kramerius-job-instance'

export interface BulkExport extends DatedObject {
	mergeJob: KrameriusJobInstance
	file?: FileRef
	state: string
}
