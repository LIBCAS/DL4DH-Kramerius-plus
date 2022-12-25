import { Button, Paper } from '@mui/material'
import {
	DataGrid,
	GridCallbackDetails,
	GridColDef,
	GridRenderCellParams,
	GridSelectionModel,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { listPublications, PublicationFilter } from 'api/publication-api'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { FC, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { dateTimeFormatter } from 'utils/formatters'

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

	const columns: GridColDef[] = [
		{
			field: 'id',
			headerName: 'UUID',
			width: 340,
		},
		{
			field: 'created',
			headerName: 'Vytvořeno',
			width: 200,
			valueGetter: dateTimeFormatter,
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
			field: 'action',
			headerName: 'Akce',
			flex: 1,
			sortable: false,
			renderCell: (params: GridRenderCellParams) => {
				const onClick = () => {
					navigate(`/publications/${params.row['id']}`)
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
			<DataGrid
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
			/>
		</Paper>
	)
}
