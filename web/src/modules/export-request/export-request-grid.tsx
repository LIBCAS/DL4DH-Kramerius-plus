import { Box, Button, Chip, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { CustomGrid } from 'components/grid/custom-grid'
import { User } from 'models/domain/user'
import { QueryResults } from 'models/query-results'
import { ExportRequest } from 'models/request/export-request'
import {
	RequestState,
	RequestStateColorMapping,
	RequestStateMapping,
} from 'models/request/request'
import { FC, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

export const ExportRequestGrid: FC<{
	isLoading: boolean
	data?: QueryResults<ExportRequest>
	pagination: GridPaginationModel
	onPaginationChange: (pagination: GridPaginationModel) => void
}> = ({ isLoading, data, pagination, onPaginationChange }) => {
	const [selection, setSelection] = useState<GridRowSelectionModel>([])

	const columns = useMemo<GridColDef<ExportRequest>[]>(
		() => [
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
				sortable: false,
				width: 350,
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
				field: 'config',
				headerName: 'Formát',
				width: 100,
				sortable: false,
				valueGetter: (params: GridValueGetterParams<ExportRequest>) =>
					params.value?.exportFormat,
			},
			{
				field: 'state',
				headerName: 'Stav žádosti',
				width: 200,
				sortable: false,
				renderCell: (params: GridRenderCellParams) => {
					const state: RequestState =
						params.row['state'] == 'RUNNING' &&
						params.row['bulkExport'].mergeJob.executionStatus == 'COMPLETED'
							? 'COMPLETED'
							: params.row['state'] // TODO: Hotfix for incorrect requestState propagation, remove after state propagation is fixed
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
							to={`/exports/${params.row['id']}`}
							variant="text"
						>
							Detail
						</Button>
					</Box>
				),
			},
		],
		[],
	)

	return (
		<Paper>
			<CustomGrid
				columns={columns}
				data={data}
				isLoading={isLoading}
				pagination={pagination}
				selection={selection}
				onPaginationChange={onPaginationChange}
				onSelectionChange={setSelection}
			/>
		</Paper>
	)
}
