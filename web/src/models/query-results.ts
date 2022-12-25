export interface QueryResults<T> {
	pageSize: number
	page: number
	total: number
	items: T[]
}
