import {
	Button,
	FormControl,
	Paper,
	Stack,
	styled,
	TextField,
} from '@mui/material'
import { usePostMessage } from 'hooks/use-post-message'
import { useState } from 'react'
import { Resolver, useForm } from 'react-hook-form'
import { Attachment } from './attachment'

const BUTTON_SIZE = 0.2

const VisuallyHiddenInput = styled('input')({
	clip: 'rect(0 0 0 0)',
	clipPath: 'inset(50%)',
	height: 1,
	overflow: 'hidden',
	position: 'absolute',
	bottom: 0,
	left: 0,
	whiteSpace: 'nowrap',
	width: 1,
})

type MessageFormType = {
	message: string
}

const resolver: Resolver<MessageFormType> = async values => {
	return {
		values: values.message ? values : {},
		errors: !values.message
			? {
					message: {
						type: 'required',
						message: 'Zpráva je povinná',
					},
			  }
			: {},
	}
}

export const MessageForm = ({ requestId }: { requestId: string }) => {
	const messageForm = useForm<MessageFormType>({
		resolver,
	})

	const postRequestMessage = usePostMessage(requestId)

	const [files, setFiles] = useState<File[]>([])

	const onCancel = () => {
		messageForm.reset()
		setFiles([])
	}

	const onRemoveFile = (index: number) => {
		const newFiles = files.filter((_, i) => i !== index)
		setFiles(newFiles)
	}

	const onSend = async (data: MessageFormType) => {
		await postRequestMessage.mutateAsync({ ...data, files: files })
		setFiles([])
		messageForm.reset()
	}

	return (
		<FormControl fullWidth>
			<form onSubmit={messageForm.handleSubmit(onSend)}>
				<Stack component={Paper} gap={4} sx={{ p: 4, width: 0.7 }}>
					<TextField
						{...messageForm.register('message')}
						error={!!messageForm.formState.errors.message}
						helperText={messageForm.formState.errors.message?.message}
						maxRows={10}
						multiline
						placeholder="Zanechte správu..."
						variant="standard"
					/>
					<Stack
						direction="row"
						display="flex"
						flexWrap="wrap"
						gap={4}
						width={1}
					>
						{files.map((file, index) => (
							<Attachment
								key={index}
								file={file}
								onRemove={() => onRemoveFile(index)}
							/>
						))}
					</Stack>
					<Stack direction="row" gap={4} justifyContent="space-between">
						<Button
							sx={{ width: BUTTON_SIZE }}
							type="submit"
							variant="contained"
						>
							Odeslat
						</Button>
						<Button
							component="label"
							sx={{ width: BUTTON_SIZE }}
							variant="contained"
						>
							Nahrát soubor(y)
							<VisuallyHiddenInput
								multiple
								type="file"
								onChange={(e: any) => setFiles([...files, ...e.target.files])}
							/>
						</Button>
						<Button
							color="error"
							sx={{ width: BUTTON_SIZE }}
							variant="contained"
							onClick={onCancel}
						>
							Zrušit
						</Button>
					</Stack>
				</Stack>
			</form>
		</FormControl>
	)
}
