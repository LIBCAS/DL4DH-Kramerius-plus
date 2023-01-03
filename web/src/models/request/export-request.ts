import { ExportJobConfig } from 'models/job/config/export-job-config'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { BulkExport } from '../bulk-export'
import { ExportRequestItem } from './export-request-item'
import { Request } from './request'

export interface ExportRequest extends Request {
	config: ExportJobConfig
	items: ExportRequestItem[]
	createRequestJob: KrameriusJobInstance
	bulkExport?: BulkExport
}
