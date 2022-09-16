import { DomainObject } from './domain-object'

export interface DatedObject extends DomainObject {
	created: string
	updated: string
	deleted?: string
}
