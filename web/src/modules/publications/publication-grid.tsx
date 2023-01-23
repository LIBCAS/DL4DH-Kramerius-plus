import { Visibility } from '@material-ui/icons'
import DeleteIcon from '@mui/icons-material/Delete'
import { Paper } from '@mui/material'
import {
	GridActionsCellItem,
	GridCallbackDetails,
	GridColumns,
	GridRowId,
	GridRowParams,
	GridSelectionModel,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { listPublications, PublicationFilter } from 'api/publication-api'
import { CustomGrid } from 'components/grid/custom-grid'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { FC, useCallback, useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'

const getModel = (params: GridValueGetterParams) => {
	return DigitalObjectModelMapping[params.row['model']]
}

export const PublicationGrid: FC<{
	filter: PublicationFilter
	allowSelection?: boolean
	onSelectionChange: (
		selectionModel: GridSelectionModel,
		details: GridCallbackDetails,
	) => void
}> = ({ filter, allowSelection, onSelectionChange }) => {
	const [rowCount, setRowCount] = useState<number>()
	const [publications, setPublications] = useState<Publication[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)
	const navigate = useNavigate()

	const toDetail = useCallback(
		(params: GridRowParams<Publication>) => () => {
			navigate(`/publications/${params.row['id']}`)
		},
		[navigate],
	)

	const deleteUser = useCallback((id: GridRowId) => () => {}, [])

	const columns = useMemo<GridColumns<Publication>>(
		() => [
			{
				field: 'id',
				headerName: 'UUID',
				width: 340,
			},
			{
				field: 'created',
				headerName: 'Vytvořeno',
				width: 200,
				type: 'dateTime',
				valueGetter: ({ value }) => value && new Date(value),
			},
			{
				field: 'title',
				headerName: 'Název',
				width: 400,
			},
			{
				field: 'model',
				headerName: 'Model',
				width: 300,
				valueGetter: getModel,
			},
			{
				field: 'actions',
				type: 'actions',
				getActions: params => [
					// eslint-disable-next-line react/jsx-key
					<GridActionsCellItem
						icon={<Visibility />}
						label="Detail"
						nonce
						showInMenu={false}
						onClick={deleteUser(params.id)}
						onResize
						onResizeCapture
					/>,
				],
			},
		],
		[toDetail],
	)

	useEffect(() => {
		async function fetchPublications() {
			const response = await listPublications(page, 10, filter)

			if (response) {
				setPublications(response.items)
				setRowCount(response.total)
			}
		}
		fetchPublications()
	}, [page, filter])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	return (
		<Paper>
			{/* <DataGrid
				autoHeight
				checkboxSelection={allowSelection}
				columns={columns}
				density="compact"
				disableColumnFilter
				disableColumnMenu
				disableSelectionOnClick={!allowSelection}
				pageSize={10}
				paginationMode="server"
				rowCount={rowCountState}
				rows={publications}
				rowsPerPageOptions={[10]}
				onPageChange={onPageChange}
				onSelectionModelChange={onSelectionChange}
				components={{ Row: }}
			/> */}
			<CustomGrid
				columns={columns}
				rowCount={rowCountState}
				rows={publications}
				onPageChange={onPageChange}
			/>
		</Paper>
	)
}
