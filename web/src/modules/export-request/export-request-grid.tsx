import { Box, Button, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { listExportRequests } from 'api/export-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { User } from 'models/domain/user'
import { QueryResults } from 'models/query-results'
import { ExportRequest } from 'models/request/export-request'
import { RequestState, RequestStateMapping } from 'models/request/request'
import { ExportRequestFilterDto } from 'pages/export/export-request-list'
import { FC, useCallback, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

export const ExportRequestGrid: FC<{ filter?: ExportRequestFilterDto }> = ({
	filter,
}) => {
	const [data, setData] = useState<QueryResults<ExportRequest>>()
	const [selection, setSelection] = useState<GridRowSelectionModel>([])
	const [pagination, setPagination] = useState<GridPaginationModel>({
		page: 0,
		pageSize: 10,
	})

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
				valueFormatter: (params: GridValueFormatterParams<RequestState>) => {
					return RequestStateMapping[params.value]
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

	const fetchRequests = useCallback(async () => {
		const response = await listExportRequests(
			pagination.page,
			pagination.pageSize,
			filter,
		)

		if (response) {
			setData(response)
		}
	}, [pagination, filter])

	useEffect(() => {
		fetchRequests()
	}, [fetchRequests])

	return (
		<Paper>
			<CustomGrid
				columns={columns}
				data={data}
				pagination={pagination}
				selection={selection}
				onPaginationChange={setPagination}
				onSelectionChange={setSelection}
			/>
		</Paper>
	)
}
