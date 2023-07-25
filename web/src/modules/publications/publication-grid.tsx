import { Tooltip } from '@material-ui/core'
import { IconButton, Paper } from '@mui/material'
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

type GridAction = {
	getIcon: (id: string) => JSX.Element
	getLabel: (id: string) => string
	getHref?: (id: string) => string
	getOnClick?: (id: string) => (e: any) => void
}

export const PublicationGrid: FC<{
	isLoading: boolean
	data?: QueryResults<Publication>
	checkboxSelection?: boolean
	pagination: GridPaginationModel
	onPaginationChange: (pagination: GridPaginationModel) => void
	selection: GridRowSelectionModel
	onSelectionChange: (selection: GridRowSelectionModel) => void
	toolbarProps?: any
	actions: GridAction[]
}> = ({
	isLoading,
	data,
	checkboxSelection,
	pagination,
	onPaginationChange,
	selection,
	onSelectionChange,
	toolbarProps,
	actions,
}) => {
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
				width: 160,
				renderCell: (params: GridRenderCellParams) => {
					return actions.map(action => {
						const id = params.row['id']
						if (action.getOnClick) {
							const handleOnClick = action.getOnClick(id)

							return (
								<Tooltip title={action.getLabel(id)}>
									<IconButton onClick={e => handleOnClick(e)}>
										{action.getIcon(id)}
									</IconButton>
								</Tooltip>
							)
						}

						if (action.getHref) {
							const href = action.getHref(id)
							return (
								<Tooltip title={action.getLabel(id)}>
									<Link rel="noopener noreferrer" to={href}>
										<IconButton>{action.getIcon(id)}</IconButton>
									</Link>
								</Tooltip>
							)
						}

						throw new Error(
							'At least one of "onClick" or "href" props must be defined.',
						)
					})
				},
			},
		],
		[actions],
	)

	return (
		<Paper>
			<CustomGrid
				checkboxSelection={checkboxSelection}
				columns={columns}
				data={data}
				isLoading={isLoading}
				pagination={pagination}
				selection={selection}
				toolbar={PublicationGridToolbar}
				toolbarProps={toolbarProps}
				onPaginationChange={onPaginationChange}
				onSelectionChange={onSelectionChange}
			/>
		</Paper>
	)
}
