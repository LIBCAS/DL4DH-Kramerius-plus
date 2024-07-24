import { useMutation, useQueryClient } from '@tanstack/react-query'
import { changeRequestState } from 'api/user-request-api'
import { RequestState } from 'models/user-requests'

export const useChangeRequestState = (requestId: string) => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationKey: ['POST', '/api/user-requests/admin', requestId],
		mutationFn: async (state: RequestState) => {
			await changeRequestState(requestId, state)
		},
		onSuccess: () => {
			queryClient.invalidateQueries(['GET', '/api/user-requests', requestId])
		},
	})
}
