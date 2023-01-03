export interface EqFilter {
	id: string // TEMP: need ID in publication-export-filters.tsx to identify filters when adding/removing
	field: string
	value: string
	operation: 'EQ'
}

export interface OrFilter {
	filters: Filter[]
	operation: 'OR'
}

export interface GtFilter {
	field: string
	value: string | number | Date
	operation: 'GT'
}

export interface AndFilter {
	filters: Filter[]
	operation: 'AND'
}

export interface LtFilter {
	field: string
	value: string | number | Date
	operation: 'LT'
}

export interface NullFilter {
	field: string
	operation: 'NULL'
}

export interface RegexFilter {
	field: string
	value: string
	operation: 'REGEX'
}

export interface InFilter {
	field: string
	values: string[] | number[] | Date[]
	operation: 'IN'
}

export type Filter =
	| EqFilter
	| OrFilter
	| GtFilter
	| AndFilter
	| LtFilter
	| NullFilter
	| RegexFilter
	| InFilter
