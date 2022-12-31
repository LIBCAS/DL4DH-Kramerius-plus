import { Sort } from '.'
import { Filter } from './filter'
import { Paging } from './paging'

export interface Params {
	paging?: Paging
	sorting: Sort[]
	filters: Filter[]
	includeFields: string[]
	excludeFields: string[]
}
