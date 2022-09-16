import { DatedObject } from './domain/dated-object'
import { ScheduledJobEvent } from './scheduled-job-event'

export interface JobPlan extends DatedObject {
	name?: string
	scheduledJobEvents: ScheduledJobEvent[]
}
