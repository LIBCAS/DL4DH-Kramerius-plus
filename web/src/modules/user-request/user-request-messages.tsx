import { Stack } from '@mui/material'
import { PageBlock } from 'components/page-block'
import { MessageDto } from 'models/user-requests'
import { MessageForm } from './message-form'
import { UserRequestMessageRow } from './user-request-message-row'

export const UserRequestMessages = ({
	requestId,
	messages,
}: {
	requestId: string
	messages: MessageDto[]
}) => {
	return (
		<PageBlock title={''}>
			<Stack gap={2}>
				<MessageForm requestId={requestId} />
				<Stack display="flex" gap={4} height={320} marginTop={4}>
					{messages.map(message => (
						<UserRequestMessageRow key={message.id} message={message} />
					))}
				</Stack>
			</Stack>
		</PageBlock>
	)
}
