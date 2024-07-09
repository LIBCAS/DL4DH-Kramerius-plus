import { useMutation, useQueryClient } from '@tanstack/react-query'
import { changeDocumentState, DocumentChangeDto } from 'api/user-request-api'

export const useChangeDocumentState = (requestId: string) => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationKey: ['PUT', '/api/user-requests/admin', requestId, '/document'],
		mutationFn: async (changeDto: DocumentChangeDto) => {
			await changeDocumentState(requestId, changeDto)
		},
		onSuccess: () => {
			queryClient.invalidateQueries(['GET', '/api/user-requests', requestId])
		},
	})
}
