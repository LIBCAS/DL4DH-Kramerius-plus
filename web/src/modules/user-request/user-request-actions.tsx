import {
	Button,
	FormControl,
	MenuItem,
	Select,
	SelectChangeEvent,
	Stack,
	Typography,
} from '@mui/material'
import { useChangeRequestState } from 'hooks/use-change-request-state'
import {
	getRequestTransitions,
	RequestState,
	UserRequestDetailDto,
} from 'models/user-requests'
import { useState } from 'react'

export const UserRequestActions = ({
	request,
}: {
	request: UserRequestDetailDto
}) => {
	const changeRequestState = useChangeRequestState(request.id)
	const [state, setState] = useState(request.state)

	const onSave = async () => {
		await changeRequestState.mutateAsync(state)
	}

	const onSelect = (event: SelectChangeEvent) => {
		setState(event.target.value as RequestState)
	}

	return (
		<FormControl fullWidth>
			<Stack gap={2} justifyContent="center">
				<Typography>Momentálny stav: {request.state}</Typography>
				{getRequestTransitions(request.state).length > 0 ? (
					<Stack direction="row" gap={4}>
						<Select defaultValue={state} fullWidth onChange={onSelect}>
							{getRequestTransitions(request.state).map((item, index) => (
								<MenuItem key={index} value={item}>
									{item}
								</MenuItem>
							))}
							<MenuItem value={request.state}>{request.state}</MenuItem>
						</Select>
						<Button
							disabled={state === request.state}
							fullWidth
							variant="contained"
							onClick={onSave}
						>
							Uložit
						</Button>
					</Stack>
				) : (
					<Typography>Žádné dostupné akce</Typography>
				)}
			</Stack>
		</FormControl>
	)
}
