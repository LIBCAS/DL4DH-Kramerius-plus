import { BulkExport } from '../bulk-export'
import { OwnedObject } from '../domain/owned-object'
import { JobPlan } from '../job-plan'

export interface ExportRequest extends OwnedObject {
	name?: string
	jobPlan?: JobPlan
	bulkExport?: BulkExport
}
