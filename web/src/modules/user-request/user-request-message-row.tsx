import { Button, Paper, Stack, TextField, Typography } from '@mui/material'
import { download } from 'api/file-ref-api'
import { FileRef } from 'models'
import { MessageDto } from 'models/user-requests'

export const UserRequestMessageRow = ({ message }: { message: MessageDto }) => {
	return (
		<Stack component={Paper} spacing={2} sx={{ p: 2 }}>
			<Stack direction="row" justifyContent="space-between">
				<Typography>Uživatel: {message.author}</Typography>
				<Typography>Vytvořeno: {message.created.toUTCString()}</Typography>
			</Stack>
			<TextField
				color="primary"
				disabled
				maxRows={10}
				multiline
				sx={{
					'& .MuiInputBase-input.Mui-disabled': {
						WebkitTextFillColor: '#000000',
					},
				}}
				value={message.message}
				variant="standard"
			/>
			{message.files.length > 0 && (
				<>
					<Typography>Prílohy:</Typography>
					<Stack direction="row">
						{message.files.map(file => (
							<MessageFile key={file.id!} file={file} />
						))}
					</Stack>
				</>
			)}
		</Stack>
	)
}

export const MessageFile = ({ file }: { file: FileRef }) => {
	return (
		<Button variant="text" onClick={download(file.id!)}>
			{file.name}
		</Button>
	)
}
