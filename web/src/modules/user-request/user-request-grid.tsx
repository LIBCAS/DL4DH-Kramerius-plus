import { Box, Button, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
} from '@mui/x-data-grid'
import { CustomGrid } from 'components/grid/custom-grid'
import { QueryResults } from 'models/query-results'
import { UserRequestListDto } from 'models/user-requests'
import { useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

type UserRequestGridProps = {
	isLoading: boolean
	data?: QueryResults<UserRequestListDto>
	pagination: GridPaginationModel
	onPaginationChange: (pagination: GridPaginationModel) => void
}

export const UserRequestGrid = ({
	isLoading,
	data,
	pagination,
	onPaginationChange,
}: UserRequestGridProps) => {
	const [selection, setSelection] = useState<GridRowSelectionModel>([])

	const columns = useMemo<GridColDef<UserRequestListDto>[]>(
		() => [
			{
				field: 'identification',
				headerName: 'Identifikace',
				width: 320,
				sortable: false,
			},
			{
				field: 'created',
				headerName: 'Vytvořeno',
				width: 320,
				type: 'dateTime',
				sortable: false,
				valueGetter: ({ value }) => value && new Date(value),
			},
			{
				field: 'updated',
				headerName: 'Změněno',
				width: 320,
				type: 'dateTime',
				sortable: false,
				valueGetter: ({ value }) => value && new Date(value),
			},
			{
				field: 'username',
				headerName: 'Vytvořil',
				width: 300,
				sortable: false,
			},
			{
				field: 'type',
				headerName: 'Typ žádosti',
				width: 250,
				sortable: false,
			},
			{
				field: 'state',
				headerName: 'Stav žádosti',
				width: 250,
				sortable: false,
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
							to={`/requests/${params.row['id']}`}
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
