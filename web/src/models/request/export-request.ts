import { DatedObject } from 'models/domain/dated-object'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { BulkExport } from '../bulk-export'
import { JobPlan } from '../job-plan'
import { ExportRequestItem } from './export-request-item'

export interface ExportRequest extends Request {
	config: any // TODO: ExportRequestConfig
	items: ExportRequestItem[]
	createRequestJob: KrameriusJobInstance
	bulkExport?: BulkExport
}
