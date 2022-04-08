export interface QueryResult<T> {
	limit: number
	offset: number
	total: number
	results: T[]
}
