import { Chip, TableCell, TableRow } from '@mui/material'
import { useChangeDocumentState } from 'hooks/use-change-document-state'
import {
	DocumentState,
	getDocumentTransitions,
	UserRequestPartDto,
} from 'models/user-requests'

export const UserRequestPartRow = ({
	part,
	requestId,
}: {
	part: UserRequestPartDto
	requestId: string
}) => {
	const changeDocumentState = useChangeDocumentState(requestId)

	const onChipClick = async (state: DocumentState) => {
		await changeDocumentState.mutateAsync({
			state,
			publicationIds: [part.publicationId],
		})
	}

	return (
		<TableRow className="data-grid-row" sx={{ p: 2 }}>
			<TableCell>{part.publicationId}</TableCell>
			<TableCell>{part.state}</TableCell>
			<TableCell>{part.note}</TableCell>
			<TableCell>
				{part.stateUntil ? part.stateUntil.toUTCString() : 'Neurčeno'}
			</TableCell>
			<TableCell>
				{getDocumentTransitions(part.state).map(transition => (
					<Chip
						key={transition}
						label={getActionName(transition as DocumentState)}
						onClick={() => onChipClick(transition as DocumentState)}
					/>
				))}
			</TableCell>
		</TableRow>
	)
}

export const getActionName = (action: DocumentState) => {
	switch (action) {
		case 'APPROVED':
			return 'Přijmout'
		case 'ENRICHED':
			return 'Označit jako zpracované'
		case 'OTHER':
			return 'Označit jako jiné'
		case 'WAITING':
			return 'Označit jako čekající'
	}
}
