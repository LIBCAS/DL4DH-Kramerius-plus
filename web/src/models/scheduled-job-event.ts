import { DatedObject } from './domain/dated-object'
import { JobEvent } from './job/job-event'

export interface ScheduledJobEvent extends DatedObject {
	order: number
	jobEvent: JobEvent
}
