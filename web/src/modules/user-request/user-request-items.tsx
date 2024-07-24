import { Chip, Stack, Typography } from '@mui/material'
import { DataGrid, GridColDef } from '@mui/x-data-grid'
import { PageBlock } from 'components/page-block'
import { useChangeDocumentState } from 'hooks/use-change-document-state'
import {
	DocumentState,
	getDocumentTransitions,
	UserRequestPartDto,
} from 'models/user-requests'
import { useEffect, useState } from 'react'

export const UserRequestItems = ({
	items,
	requestId,
}: {
	items: UserRequestPartDto[]
	requestId: string
}) => {
	const [selected, setSelected] = useState<string[]>([])
	const [possibleStates, setPossibleStates] = useState<DocumentState[]>([])
	const changeDocumentState = useChangeDocumentState(requestId)

	const getSelectedItems = () =>
		items.filter(item => selected.includes(item.publicationId))

	const getPossibleStates = () => {
		if (selected.length === 0) {
			return []
		}

		const items = getSelectedItems()
		let result = getDocumentTransitions(items[0].state)

		items.forEach(item => {
			const next = getDocumentTransitions(item.state)
			result = result.filter(state => next.includes(state))
		})

		return result
	}

	const onChipClick = async (state: DocumentState) => {
		await changeDocumentState.mutateAsync({
			state,
			publicationIds: selected,
		})
	}

	useEffect(() => {
		setPossibleStates(getPossibleStates())
	}, [selected, items])

	const cols: GridColDef[] = [
		{ field: 'publicationId', headerName: 'UUID Publikace', width: 320 },
		{ field: 'state', headerName: 'Stav', width: 100 },
		{ field: 'note', headerName: 'Poznámka', width: 350 },
		{ field: 'stateUntil', headerName: 'Stav do', width: 250 },
	]

	return (
		<PageBlock title="Položky žádosti">
			<Stack gap={2}>
				{possibleStates.length > 0 ? (
					<Stack alignItems="center" direction="row" gap={2}>
						<Typography>Dostupné akce:</Typography>
						{possibleStates.map(state => (
							<Chip
								key={state}
								label={getActionName(state)}
								onClick={() => onChipClick(state)}
							/>
						))}
					</Stack>
				) : (
					<Typography>Žádné dostupné akce</Typography>
				)}
				{items && (
					<DataGrid
						checkboxSelection
						columns={cols}
						getRowId={row => row.publicationId}
						rows={items}
						onRowSelectionModelChange={ids => {
							setSelected(ids.map(id => id as string))
						}}
					/>
				)}
			</Stack>
		</PageBlock>
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
