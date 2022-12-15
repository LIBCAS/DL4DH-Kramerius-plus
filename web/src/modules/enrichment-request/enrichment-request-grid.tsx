import { Button, Icon, Paper } from '@mui/material'
import {
	DataGrid,
	GridActionsCellItem,
	GridRenderCellParams,
	GridRowParams,
	GridValueFormatterParams,
} from '@mui/x-data-grid'
import { listEnrichmentRequests } from 'api/enrichment-api'
import { User } from 'models/domain/user'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { EnrichmentRequestFilterDto } from 'pages/enrichment/enrichment-request-list'
import { FC, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Icons } from 'react-toastify'
import { dateTimeFormatter } from 'utils/formatters'

export const EnrichmentRequestGrid: FC<{
	filter: EnrichmentRequestFilterDto
}> = ({ filter }) => {
	const [rowCount, setRowCount] = useState<number>()
	const [enrichmentRequests, setEnrichmentRequests] = useState<
		EnrichmentRequest[]
	>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)
	const navigate = useNavigate()

	const columns = [
		{
			field: 'created',
			headerName: 'Vytvořeno',
			width: 250,
			valueFormatter: dateTimeFormatter,
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
		},
	]

	useEffect(() => {
		async function fetchRequests() {
			const response = await listEnrichmentRequests(page, 10, filter)

			if (response) {
				setEnrichmentRequests(response.results)
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
				rows={enrichmentRequests}
				rowsPerPageOptions={[]}
				onPageChange={onPageChange}
				onRowClick={(params: GridRowParams) => navigate(params.row['id'])}
			/>
		</Paper>
	)
}
