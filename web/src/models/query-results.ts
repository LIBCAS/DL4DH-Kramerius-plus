export interface QueryResults<T> {
	limit: number
	offset: number
	total: number
	results: T[]
}
