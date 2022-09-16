import { OwnedObject } from '../domain/owned-object'
import { JobPlan } from '../job-plan'

export interface EnrichmentRequest extends OwnedObject {
	name?: string
	jobPlans: JobPlan[]
}
