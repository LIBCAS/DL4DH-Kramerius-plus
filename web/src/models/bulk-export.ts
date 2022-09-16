import { Export, FileRef } from 'models'
import { DatedObject } from './domain/dated-object'

export interface BulkExport extends DatedObject {
	fileRef?: FileRef
	exports: Export[]
}
