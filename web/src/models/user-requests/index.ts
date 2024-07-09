import { fileRefSchema } from 'models/fileRef'
import { z } from 'zod'

export const UserRequestPartDtoSchema = z.object({
	publicationId: z.string(),
	state: z.enum(['WAITING', 'APPROVED', 'ENRICHED', 'OTHER']),
	note: z.string(),
	stateUntil: z.coerce.date().optional(),
})

export const MessageDtoSchema = z.object({
	id: z.string(),
	files: z.array(fileRefSchema),
	message: z.string(),
	author: z.string(),
	created: z.coerce.date(),
	updated: z.coerce.date(),
})

export const requestStates = [
	'CREATED',
	'IN_PROGRESS',
	'APPROVED',
	'WAITING_FOR_USER',
	'PROLONGING',
	'REJECTED',
]

export const UserRequestListDtoSchema = z.object({
	id: z.string(),
	created: z.coerce.date(),
	updated: z.coerce.date(),
	username: z.string(),
	type: z.enum(['EXPORT', 'ENRICHMENT']),
	state: z.enum([
		'CREATED',
		'IN_PROGRESS',
		'APPROVED',
		'WAITING_FOR_USER',
		'PROLONGING',
		'REJECTED',
	]),
	identification: z.string(),
})

export const UserRequestDetailDtoSchema = UserRequestListDtoSchema.extend({
	parts: z.array(UserRequestPartDtoSchema),
	messages: z.array(MessageDtoSchema),
})

export type UserRequestPartDto = z.infer<typeof UserRequestPartDtoSchema>
export type MessageDto = z.infer<typeof MessageDtoSchema>
export type UserRequestListDto = z.infer<typeof UserRequestListDtoSchema>
export type UserRequestDetailDto = z.infer<typeof UserRequestDetailDtoSchema>

export function createPaginatedResponseSchema<ItemType extends z.ZodTypeAny>(
	itemSchema: ItemType,
) {
	return z.object({
		page: z.number(),
		pageSize: z.number(),
		total: z.number(),
		items: z.array(itemSchema),
	})
}

export type DocumentState = 'WAITING' | 'APPROVED' | 'ENRICHED' | 'OTHER'

export const getDocumentTransitions = (state: DocumentState) => {
	switch (state) {
		case 'WAITING':
			return ['APPROVED']
		case 'APPROVED':
			return ['ENRICHED', 'OTHER']
		case 'ENRICHED':
			return ['OTHER']
		case 'OTHER':
			return ['APPROVED', 'ENRICHED']
	}
}

export type RequestState =
	| 'CREATED'
	| 'IN_PROGRESS'
	| 'WAITING_FOR_USER'
	| 'APPROVED'
	| 'PROLONGING'
	| 'REJECTED'

export type RequestType = 'EXPORT' | 'ENRICHMENT'

export const getRequestTransitions = (state: RequestState) => {
	switch (state) {
		case 'CREATED':
			return ['IN_PROGRESS', 'WAITING_FOR_USER']
		case 'IN_PROGRESS':
			return ['APPROVED', 'REJECTED', 'WAITING_FOR_USER']
		case 'WAITING_FOR_USER':
		case 'PROLONGING':
			return ['IN_PROGRESS']
		case 'APPROVED':
			return ['PROLONGING']
		case 'REJECTED':
			return []
	}
}
