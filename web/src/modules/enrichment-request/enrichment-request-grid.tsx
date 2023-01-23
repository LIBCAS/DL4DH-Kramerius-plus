import { Box, Button, Paper } from '@mui/material'
import {
	GridColumns,
	GridRenderCellParams,
	GridValueFormatterParams,
} from '@mui/x-data-grid'
import { listEnrichmentRequests } from 'api/enrichment-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { User } from 'models/domain/user'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { RequestState, RequestStateMapping } from 'models/request/request'
import { EnrichmentRequestFilterDto } from 'pages/enrichment/enrichment-request-list'
import React, { FC, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

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

	const columns = useMemo<GridColumns<EnrichmentRequest>>(
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
							to={`/enrichment/${params.row['id']}`}
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
			const response = await listEnrichmentRequests(page, 10, filter)

			if (response) {
				setEnrichmentRequests(response.items)
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
				rows={enrichmentRequests}
				onPageChange={onPageChange}
			/>
		</Paper>
	)
}
