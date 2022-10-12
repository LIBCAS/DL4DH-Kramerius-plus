import { Button, Paper } from '@mui/material'
import {
	DataGrid,
	GridRenderCellParams,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { listExportRequests } from 'api/export-api'
import { BulkExport } from 'models/bulk-export'
import { ExportRequest } from 'models/export-request/export-request'
import { ExportRequestFilterDto } from 'models/export-request/export-request-filter-dto'
import { FC, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { dateTimeFormatter, ownerFormatter } from 'utils/formatters'

const exportDoneFormatter = (params: GridValueFormatterParams) => {
	if ((params.value as BulkExport).fileRef) {
		return 'ANO'
	}

	return 'NE'
}

export const ExportRequestList: FC<{ filter: ExportRequestFilterDto }> = ({
	filter,
}) => {
	const [rowCount, setRowCount] = useState<number>()
	const [exportRequests, setExportRequests] = useState<ExportRequest[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)
	const navigate = useNavigate()

	const columns = [
		{
			field: 'id',
			headerName: 'ID',
			width: 400,
			type: 'string',
		},
		{
			field: 'created',
			headerName: 'Vytvořeno',
			width: 250,
			type: 'string',
			valueFormatter: dateTimeFormatter,
		},
		{
			field: 'owner',
			headerName: 'Vytvořil',
			width: 250,
			type: 'string',
			valueFormatter: ownerFormatter,
		},
		{
			field: 'name',
			headerName: 'Název',
			width: 350,
			type: 'string',
		},
		{
			field: 'bulkExportFormat',
			headerName: 'Formát',
			width: 200,
			type: 'string',
			valueGetter: (params: GridValueGetterParams) => {
				return (
					exportRequests.find(request => request.id === params.id)?.bulkExport
						?.format ?? '-'
				)
			},
		},
		{
			field: 'bulkExport',
			headerName: 'Ukončen',
			width: 200,
			type: 'string',
			valueFormatter: exportDoneFormatter,
		},
		{
			field: 'action',
			headerName: 'Akce',
			flex: 1,
			sortable: false,
			renderCell: (params: GridRenderCellParams) => {
				const onClick = () => {
					navigate(params.row['id'])
				}
				return (
					<Button color="primary" variant="contained" onClick={onClick}>
						Detail
					</Button>
				)
			},
		},
	]

	useEffect(() => {
		async function fetchRequests() {
			const response = await listExportRequests(page, 10, filter)

			if (response) {
				setExportRequests(response.results)
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
			<DataGrid
				autoHeight
				columns={columns}
				density="compact"
				disableColumnFilter
				disableColumnMenu
				disableSelectionOnClick
				pageSize={10}
				paginationMode="server"
				rowCount={rowCountState}
				rows={exportRequests}
				rowsPerPageOptions={[]}
				onPageChange={onPageChange}
			/>
		</Paper>
	)
}
