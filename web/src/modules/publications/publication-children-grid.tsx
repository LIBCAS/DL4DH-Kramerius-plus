import { Box, Button, Paper } from '@mui/material'
import {
	GridColumns,
	GridRenderCellParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { listPublications } from 'api/publication-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { FC, useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'

export const PublicationChildrenGrid: FC<{
	parentId: string
}> = ({ parentId }) => {
	const [rowCount, setRowCount] = useState<number>()
	const [publications, setPublications] = useState<Publication[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)

	const columns = useMemo<GridColumns<Publication>>(
		() => [
			{
				field: 'id',
				headerName: 'UUID',
				maxWidth: 400,
				flex: 1,
			},
			{
				field: 'created',
				headerName: 'Vytvořeno',
				maxWidth: 200,
				flex: 0.5,
				type: 'dateTime',
				valueGetter: ({ value }) => value && new Date(value),
			},
			{
				field: 'title',
				headerName: 'Název',
				flex: 1,
			},
			{
				field: 'model',
				headerName: 'Model',
				maxWidth: 200,
				flex: 0.6,
				valueGetter: (params: GridValueGetterParams) =>
					DigitalObjectModelMapping[params.row['model']],
			},
			{
				field: 'publishInfo',
				headerName: 'Publikováno',
				maxWidth: 120,
				type: 'boolean',
				flex: 0.6,
				valueGetter: (params: GridValueGetterParams) =>
					params.row['publishInfo']['isPublished'],
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
		async function fetchPublications() {
			const response = await listPublications(page, 10, { parentId })

			if (response) {
				setPublications(response.items)
				setRowCount(response.total)
			}
		}
		fetchPublications()
	}, [page, parentId])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	return (
		<Paper>
			<CustomGrid
				columns={columns}
				rowCount={rowCountState}
				rows={publications}
				onPageChange={onPageChange}
			/>
		</Paper>
	)
}
