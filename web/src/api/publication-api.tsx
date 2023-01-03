import { DigitalObjectModel } from 'enums/publication-model'
import { Publication } from 'models'
import { QueryResults } from 'models/query-results'
import { customFetch } from 'utils/custom-fetch'

export type PublicationFilter = {
	uuid?: string
	title?: string
	parentId?: string
	model?: DigitalObjectModel
	createdBefore?: Date
	createdAfter?: Date
	isPublished?: boolean
	publishedBefore?: Date
	publishedAfter?: Date
	isRootEnrichment?: boolean
}

export const getPublication = async (
	publicationId: string,
): Promise<Publication> => {
	const response = await customFetch(`/api/publications/${publicationId}`, {
		method: 'GET',
	})
	return await response?.json()
}

export const listPublications = async (
	page: number,
	pageSize: number,
	filter?: PublicationFilter,
): Promise<QueryResults<Publication>> => {
	const filterCopy = { ...filter }
	if (filterCopy && !filterCopy.isPublished) {
		filterCopy.publishedAfter = undefined
		filterCopy.publishedBefore = undefined
	}
	const filterParams = filterCopy
		? Object.entries(filterCopy)
				.map(([key, value]) =>
					value
						? `&${key}=${value instanceof Date ? value.toISOString() : value}`
						: '',
				)
				.join('')
		: ''
	const url = `/api/publications/list?page=${page}&pageSize=${pageSize}${filterParams}`

	const response = await customFetch(url, {
		method: 'GET',
	})

	return await response?.json()
}

export const publish = async (publicationId: string): Promise<Response> => {
	return await customFetch(`/api/publications/${publicationId}/publish`, {
		method: 'POST',
	})
}

export const unpublish = async (publicationId: string): Promise<Response> => {
	return await customFetch(`/api/publications/${publicationId}/unpublish`, {
		method: 'POST',
	})
}
