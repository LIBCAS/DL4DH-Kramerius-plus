import { QueryResults } from 'models/query-results'
import {
	createPaginatedResponseSchema,
	DocumentState,
	RequestState,
	RequestType,
	UserRequestDetailDto,
	UserRequestDetailDtoSchema,
	UserRequestListDto,
	UserRequestListDtoSchema,
} from 'models/user-requests'
import { customFetch, customFetchWithContentType } from 'utils/custom-fetch'

const path = '/api/user-requests'

export type UserRequestListFilter = {
	viewDeleted?: boolean
	year?: number
	identification?: number
	type?: RequestType
	state?: RequestState
	username?: string
	sortOrder?: OrderType
	sortField?: SortField
	rootFilterOperation?: RootFilterOperation
}

export type OrderType = 'ASC' | 'DESC'
export type SortField = 'CREATED' | 'UPDATED' | 'AUTHOR'
export type RootFilterOperation = 'AND' | 'OR'

export const list = async (
	page: number,
	pageSize: number,
	filter: UserRequestListFilter,
): Promise<QueryResults<UserRequestListDto>> => {
	const viewDeleted = filter.viewDeleted
		? `&viewDeleted=${filter.viewDeleted}`
		: `&viewDeleted=false`
	const year = filter.year ? `&year=${filter.year}` : ''
	const identification = filter.identification
		? `&identification=${filter.identification}`
		: ''
	const state = filter.state ? `&state=${filter.state}` : ''
	const username = filter.username ? `&username=${filter.username}` : ''
	const sortOrder = filter.sortOrder ? `&sortOrder=${filter.sortOrder}` : ''
	const sortField = filter.sortField ? `&sortField=${filter.sortField}` : ''
	const rootFilterOperation = filter.rootFilterOperation
		? `&rootFilterOperation=${filter.rootFilterOperation}`
		: ''
	const type = filter.type ? `&type=${filter.type}` : ''

	const response = await customFetch(
		path +
			'/' +
			`?page=${page}&pageSize=${pageSize}` +
			viewDeleted +
			year +
			identification +
			state +
			username +
			sortOrder +
			sortField +
			rootFilterOperation +
			type,
		{
			method: 'GET',
		},
	)

	if (!response.ok) {
		throw new Error('Failed to get user request list')
	}

	return await createPaginatedResponseSchema(
		UserRequestListDtoSchema,
	).parseAsync(await response.json())
}

export const getById = async (id: string): Promise<UserRequestDetailDto> => {
	const response = await customFetch(path + '/' + id, {
		method: 'GET',
	})

	if (!response.ok) {
		throw new Error('Failed to get user request detail')
	}

	return await UserRequestDetailDtoSchema.parseAsync(await response.json())
}

export type MessageType = {
	message: string
	files: File[]
}

export const postRequestMessage = async (
	requestId: string,
	messageForm: MessageType,
): Promise<void> => {
	const data = new FormData()

	data.append('message', messageForm.message)
	messageForm.files.forEach(file => {
		data.append('files', file, file.name)
	})

	const response = await customFetchWithContentType(
		`/api/user-requests/${requestId}/message`,
		{
			method: 'POST',
			body: data,
		},
	)

	if (!response.ok) {
		throw new Error('Failed to post message')
	}
}

export const changeRequestState = async (
	requestId: string,
	state: RequestState,
): Promise<void> => {
	const response = await customFetch(
		`/api/user-requests/admin/${requestId}?state=${state}`,
		{
			method: 'PUT',
		},
	)

	if (!response.ok) {
		throw new Error('Failed to change request state')
	}
}

export type DocumentChangeDto = {
	publicationIds: string[]
	state: DocumentState
}

export const changeDocumentState = async (
	requestId: string,
	changeDto: DocumentChangeDto,
) => {
	const response = await customFetch(
		`/api/user-requests/admin/${requestId}/document`,
		{
			method: 'PUT',
			body: JSON.stringify(changeDto),
		},
	)

	if (!response.ok) {
		throw new Error('Failed to change request state')
	}
}
