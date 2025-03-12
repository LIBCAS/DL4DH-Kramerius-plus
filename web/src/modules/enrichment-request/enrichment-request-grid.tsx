import { FC, useEffect, useState } from 'react'
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
import _ from 'lodash'

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
	const navigate = useNavigate()

	const [selection, setSelection] = useState<GridRowSelectionModel>([])
	const [selectedRequests, setSelectedRequests] = useState<EnrichmentRequest[]>(
		[],
	)

	useEffect(() => {
		const toAdd =
			data?.items.filter?.(item => selection.find(id => id === item.id)) ?? []
		setSelectedRequests(prev => {
			// filter unchecked
			const withoutUnchecked =
				prev.filter(p => selection.find(id => id === p.id)) ?? []

			return _.uniqBy([...withoutUnchecked, ...toAdd], 'id')
		})
	}, [data?.items, selection])

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
					onEnrchichmentRequestClick,
					onSelectPartialFailed,
					onSelectFailed,
				}) => {
					return (
						<GridToolbarContainer>
							<Button onClick={() => onClearSelection?.()}>Zrušit výběr</Button>
							<Button onClick={() => onSelectPartialFailed?.()}>
								Označit částečně selhané
							</Button>
							<Button onClick={() => onSelectFailed?.()}>
								Označit selhané
							</Button>
							<Button
								disabled={selection.length === 0}
								variant="contained"
								onClick={() => onEnrchichmentRequestClick?.()}
							>
								Obohatit označené ({selection.length})
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
					onSelectPartialFailed: () =>
						setSelection(prev => {
							const partialFailed =
								data?.items
									.filter(item => item.state === 'PARTIAL')
									.map(item => item.id) ?? []
							return _.uniq([...prev, ...partialFailed])
						}),
					onSelectFailed: () =>
						setSelection(prev => {
							const partialFailed =
								data?.items
									.filter(item => item.state === 'FAILED')
									.map(item => item.id) ?? []
							return _.uniq([...prev, ...partialFailed])
						}),
					onEnrchichmentRequestClick: () => {
						navigate('/enrichment/new', {
							state: { requests: selectedRequests },
						})
					},
				}}
				onPaginationChange={onPaginationChange}
				onSelectionChange={setSelection}
			/>
		</Paper>
	)
}
