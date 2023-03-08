import { Box, Button, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { listPublications } from 'api/publication-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { QueryResults } from 'models/query-results'
import { FC, useCallback, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

export const PublicationChildrenGrid: FC<{
	parentId: string
}> = ({ parentId }) => {
	const [data, setData] = useState<QueryResults<Publication>>()
	const [pagination, setPagination] = useState<GridPaginationModel>({
		page: 0,
		pageSize: 10,
	})
	const [selection, setSelection] = useState<GridRowSelectionModel>([])

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

	const fetchPublications = useCallback(async () => {
		const response = await listPublications(
			pagination.page,
			pagination.pageSize,
			{ parentId },
		)

		if (response) {
			setData(response)
		}
	}, [pagination, parentId])

	useEffect(() => {
		fetchPublications()
	}, [fetchPublications])

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
