import { Box, Button, Paper } from '@mui/material'
import {
	GridColDef,
	GridPaginationModel,
	GridRenderCellParams,
	GridRowSelectionModel,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { CustomGrid } from 'components/grid/custom-grid'
import { PublicationGridToolbar } from 'components/grid/publication-grid-toolbar'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { QueryResults } from 'models/query-results'
import { FC, useMemo } from 'react'
import { Link } from 'react-router-dom'

type GridProps = {
	data?: QueryResults<Publication>
	checkboxSelection?: boolean
	pagination: GridPaginationModel
	onPaginationChange: (pagination: GridPaginationModel) => void
	selection: GridRowSelectionModel
	onSelectionChange: (selection: GridRowSelectionModel) => void
	toolbarProps?: any
}

export const PublicationGrid: FC<{
	onExportClick: (publicationId: string) => (e: React.MouseEvent) => void
	onPublishClick: (publicationId: string) => (e: React.MouseEvent) => void
	gridProps: GridProps
}> = ({ onExportClick, onPublishClick, gridProps }) => {
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
				type: 'dateTime',
				sortable: false,
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
				sortable: false,
				maxWidth: 200,
				flex: 0.6,
				valueGetter: (params: GridValueGetterParams) =>
					DigitalObjectModelMapping[params.row['model']],
			},
			{
				field: 'publishInfo',
				headerName: 'Publikováno',
				sortable: false,
				maxWidth: 120,
				type: 'boolean',
				flex: 0.6,
				valueGetter: (params: GridValueGetterParams) =>
					params.row['publishInfo']['isPublished'],
			},
			{
				field: 'actions',
				headerName: 'Akce',
				sortable: false,
				width: 300,
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
						<Button
							size="small"
							variant="text"
							onClick={onExportClick(params.row['id'])}
						>
							Exportovat
						</Button>
						<Button
							size="small"
							variant="text"
							onClick={onPublishClick(params.row['id'])}
						>
							Publikovat
						</Button>
					</Box>
				),
			},
		],
		[onExportClick, onPublishClick],
	)

	return (
		<Paper>
			<CustomGrid
				columns={columns}
				toolbar={PublicationGridToolbar}
				{...gridProps}
			/>
		</Paper>
	)
}
