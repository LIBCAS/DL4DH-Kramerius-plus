import { Box, Button, Paper } from '@mui/material'
import {
	GridColumns,
	GridRenderCellParams,
	GridValueFormatterParams,
} from '@mui/x-data-grid'
import { listExportRequests } from 'api/export-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { User } from 'models/domain/user'
import { ExportRequest } from 'models/request/export-request'
import { RequestState, RequestStateMapping } from 'models/request/request'
import { ExportRequestFilterDto } from 'pages/export/export-request-list'
import { FC, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

export const ExportRequestGrid: FC<{ filter: ExportRequestFilterDto }> = ({
	filter,
}) => {
	const [rowCount, setRowCount] = useState<number>()
	const [exportRequests, setExportRequests] = useState<ExportRequest[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)

	const columns = useMemo<GridColumns<ExportRequest>>(
		() => [
			{
				field: 'created',
				headerName: 'Vytvořeno',
				maxWidth: 200,
				flex: 0.5,
				type: 'dateTime',
				valueGetter: ({ value }) => value && new Date(value),
			},
			{
				field: 'owner',
				headerName: 'Vytvořil',
				width: 250,
				valueFormatter: (params: GridValueFormatterParams<User>) =>
					params.value.username,
			},
			{
				field: 'name',
				headerName: 'Název',
				width: 350,
			},
			{
				field: 'publicationIds',
				headerName: 'Publikace v žádosti',
				width: 800,
				flex: 4,
				valueFormatter: (params: GridValueFormatterParams<string[]>) =>
					params.value.join(', '),
			},
			{
				field: 'state',
				headerName: 'Stav žádosti',
				width: 250,
				valueFormatter: (params: GridValueFormatterParams<RequestState>) => {
					return RequestStateMapping[params.value]
				},
			},
			{
				field: 'actions',
				headerName: 'Akce',
				width: 100,
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

	useEffect(() => {
		async function fetchRequests() {
			const response = await listExportRequests(page, 10, filter)

			if (response) {
				setExportRequests(response.items)
				setRowCount(response.total)
			}
		}
		fetchRequests()
	}, [page, filter])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	return (
		<Paper>
			<CustomGrid
				checkboxSelection={false}
				columns={columns}
				rowCount={rowCountState}
				rows={exportRequests}
				onPageChange={onPageChange}
			/>
		</Paper>
	)
}
