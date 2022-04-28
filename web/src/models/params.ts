import { Sort } from '.'
import { Filter } from './filter'
import { Paging } from './paging'

export interface Params {
	paging?: Paging
	sort?: Sort
	filters?: Filter[]
	includeFields?: string[]
	excludeFields?: string[]
}
