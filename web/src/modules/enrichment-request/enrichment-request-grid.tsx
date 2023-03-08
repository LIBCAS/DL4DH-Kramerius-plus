import { Box, Button, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
	GridValueFormatterParams,
} from '@mui/x-data-grid'
import { listEnrichmentRequests } from 'api/enrichment-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { User } from 'models/domain/user'
import { QueryResults } from 'models/query-results'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { RequestState, RequestStateMapping } from 'models/request/request'
import { EnrichmentRequestFilterDto } from 'pages/enrichment/enrichment-request-list'
import React, { FC, useCallback, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

export const EnrichmentRequestGrid: FC<{
	filter: EnrichmentRequestFilterDto
}> = ({ filter }) => {
	const [data, setData] = useState<QueryResults<EnrichmentRequest>>()
	const [selection, setSelection] = useState<GridRowSelectionModel>([])
	const [pagination, setPagination] = useState<GridPaginationModel>({
		page: 0,
		pageSize: 10,
	})

	const columns = useMemo<GridColDef<EnrichmentRequest>[]>(
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

	const fetchRequests = useCallback(
		async (pagination: GridPaginationModel) => {
			const response = await listEnrichmentRequests(
				pagination.page,
				pagination.pageSize,
				filter,
			)

			if (response) {
				setData(response)
			}
		},
		[filter],
	)

	useEffect(() => {
		fetchRequests(pagination)
	}, [fetchRequests, pagination])

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
