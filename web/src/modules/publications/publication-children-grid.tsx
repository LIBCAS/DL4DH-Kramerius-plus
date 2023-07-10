import { Box, Button, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { listPublications } from 'api/publication-api'
import { CustomErrorComponent } from 'components/custom-error-component'
import { CustomGrid } from 'components/grid/custom-grid'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { FC, useEffect, useMemo, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'

export const PublicationChildrenGrid: FC<{
	parentId: string
}> = ({ parentId }) => {
	const queryClient = useQueryClient()
	const [searchParams, setSearchParams] = useSearchParams()
	const [selection, setSelection] = useState<GridRowSelectionModel>([])
	const { status, isLoading, data, error, isPreviousData } = useQuery({
		queryKey: [
			'publications',
			parseInt(searchParams.get('page') ?? '1') - 1,
			parseInt(searchParams.get('pageSize') ?? '10'),
			{ parentId },
		],
		queryFn: () =>
			listPublications(
				parseInt(searchParams.get('page') ?? '1') - 1,
				parseInt(searchParams.get('pageSize') ?? '10'),
				{ parentId },
			),
		keepPreviousData: true,
		staleTime: 5000,
	})

	const columns = useMemo<GridColDef<Publication>[]>(
		() => [
			{
				field: 'id',
				headerName: 'UUID',
				maxWidth: 400,
				sortable: false,
				flex: 1,
			},
			{
				field: 'created',
				headerName: 'Vytvořeno',
				maxWidth: 200,
				flex: 0.5,
				sortable: false,
				type: 'dateTime',
				valueGetter: ({ value }) => value && new Date(value),
			},
			{
				field: 'title',
				headerName: 'Název',
				sortable: false,
				flex: 1,
			},
			{
				field: 'model',
				headerName: 'Model',
				maxWidth: 200,
				sortable: false,
				flex: 0.6,
				valueGetter: (params: GridValueGetterParams) =>
					DigitalObjectModelMapping[params.row['model']],
			},
			{
				field: 'publishInfo',
				headerName: 'Publikováno',
				maxWidth: 120,
				type: 'boolean',
				sortable: false,
				flex: 0.6,
				valueGetter: (params: GridValueGetterParams) =>
					params.row['publishInfo']['isPublished'],
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
							to={`/publications/${params.row['id']}`}
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
		const page = parseInt(searchParams.get('page') ?? '1') - 1
		const pageSize = parseInt(searchParams.get('pageSize') ?? '10')
		if (
			!isPreviousData &&
			data &&
			data.total > (data.page + 1) * data.pageSize
		) {
			queryClient.prefetchQuery({
				queryKey: ['enrichmentRequests', page + 1, pageSize],
				queryFn: () => listPublications(page + 1, pageSize, { parentId }),
			})
		}
	}, [data, isPreviousData, searchParams, queryClient, parentId])

	const onPaginationChange = ({ page, pageSize }: GridPaginationModel) => {
		setSearchParams({
			...(page !== 0 ? { page: `${page + 1}` } : {}),
			...(pageSize !== 10 ? { pageSize: `${pageSize}` } : {}),
		})
	}

	return (
		<Paper>
			{status === 'error' ? (
				<CustomErrorComponent error={error} />
			) : (
				<CustomGrid
					columns={columns}
					data={data}
					isLoading={isLoading}
					pagination={{
						page: parseInt(searchParams.get('page') ?? '1') - 1,
						pageSize: parseInt(searchParams.get('pageSize') ?? '10'),
					}}
					selection={selection}
					onPaginationChange={onPaginationChange}
					onSelectionChange={setSelection}
				/>
			)}
		</Paper>
	)
}
