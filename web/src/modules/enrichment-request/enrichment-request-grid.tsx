import { FC, useState } from 'react'
import { Box, Button, Chip, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
	GridToolbarContainer,
	GridValueFormatterParams,
} from '@mui/x-data-grid'
import { CustomGrid } from 'components/grid/custom-grid'
import { User } from 'models/domain/user'
import { QueryResults } from 'models/query-results'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import {
	RequestState,
	RequestStateColorMapping,
	RequestStateMapping,
} from 'models/request/request'
import { Link, useNavigate } from 'react-router-dom'

const columns: GridColDef<EnrichmentRequest>[] = [
	{
		field: 'created',
		headerName: 'Vytvořeno',
		width: 160,
		type: 'dateTime',
		sortable: false,
		valueGetter: ({ value }) => value && new Date(value),
	},
	{
		field: 'owner',
		headerName: 'Vytvořil',
		width: 150,
		sortable: false,
		valueFormatter: (params: GridValueFormatterParams<User>) =>
			params.value.username,
	},
	{
		field: 'name',
		headerName: 'Název',
		width: 350,
		sortable: false,
	},
	{
		field: 'publicationIds',
		headerName: 'Publikace v žádosti',
		flex: 1,
		sortable: false,
		valueFormatter: (params: GridValueFormatterParams<string[]>) =>
			params.value.join(', '),
	},
	{
		field: 'state',
		headerName: 'Stav žádosti',
		width: 200,
		sortable: false,
		renderCell: (params: GridRenderCellParams) => {
			const state: RequestState = params.row['state']
			return (
				<Chip
					color={RequestStateColorMapping[state]}
					label={RequestStateMapping[state]}
					style={{ opacity: '80%' }}
				/>
			)
		},
	},
	{
		field: 'actions',
		headerName: 'Akce',
		width: 100,
		sortable: false,
		renderCell: (params: GridRenderCellParams) => (
			<Box display="flex" justifyContent="space-between" width="100%">
				<Button
					component={Link}
					size="small"
					to={`/enrichment/${params.row['id']}`}
					variant="text"
				>
					Detail
				</Button>
			</Box>
		),
	},
]

export const EnrichmentRequestGrid: FC<{
	isLoading: boolean
	data?: QueryResults<EnrichmentRequest>
	pagination: GridPaginationModel
	onPaginationChange: (pagination: GridPaginationModel) => void
}> = ({ isLoading, data, pagination, onPaginationChange }) => {
	const [selection, setSelection] = useState<GridRowSelectionModel>([])

	const navigate = useNavigate()

	return (
		<Paper>
			<CustomGrid
				checkboxSelection={true}
				columns={columns}
				data={data}
				isLoading={isLoading}
				pagination={pagination}
				selection={selection}
				toolbar={({
					onClearSelection,
					onClearCompleted,
					onEnrchichmentRequestClick,
				}) => {
					return (
						<GridToolbarContainer>
							<Button onClick={() => onClearSelection?.()}>Zrušit výběr</Button>
							<Button onClick={() => onClearCompleted?.()}>
								Zrušit výběr dokončených
							</Button>
							<Button onClick={() => onEnrchichmentRequestClick?.()}>
								Obohatit označené
							</Button>
						</GridToolbarContainer>
					)
				}}
				toolbarProps={{
					onClearSelection: () => setSelection([]),
					onClearCompleted: () =>
						setSelection(prev => {
							const filtered = prev.filter(
								id => data?.items.find(p => p.id === id)?.state !== 'COMPLETED',
							)
							return filtered
						}),
					onEnrchichmentRequestClick: () => {
						const requests = selection
							.map(id => data?.items.find(item => id === item.id))
							.filter(f => f !== undefined) as EnrichmentRequest[]

						navigate('/enrichment/new', { state: { requests } })
					},
				}}
				onPaginationChange={onPaginationChange}
				onSelectionChange={setSelection}
			/>
		</Paper>
	)
}
