import { Box, Button, Paper } from '@mui/material'
import {
	GridColumns,
	GridRenderCellParams,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { listExportRequests } from 'api/export-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { User } from 'models/domain/user'
import { ExportJobConfig } from 'models/job/config/export-job-config'
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
				valueGetter: (params: GridValueGetterParams<ExportJobConfig>) =>
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
