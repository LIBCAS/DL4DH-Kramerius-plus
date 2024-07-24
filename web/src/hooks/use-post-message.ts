import { useMutation, useQueryClient } from '@tanstack/react-query'
import { MessageType, postRequestMessage } from 'api/user-request-api'

export const usePostMessage = (requestId: string) => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationKey: ['POST', '/api/user-requests', requestId, '/messages'],
		mutationFn: async (messageForm: MessageType) => {
			await postRequestMessage(requestId, messageForm)
		},
		onSuccess: () => {
			queryClient.invalidateQueries(['GET', '/api/user-requests', requestId])
		},
	})
}
