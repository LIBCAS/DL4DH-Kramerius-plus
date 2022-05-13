import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { Publication } from 'models'
import { useEffect, useState } from 'react'
import { listPublications } from './publication-api'

type Props = {
	onRowClick: (params: GridRowParams) => void
}

const getModel = (params: GridValueGetterParams) => {
	console.log(params.row['model'])
	return params.row['model']
}

const columns: GridColDef[] = [
	{
		field: 'id',
		headerName: 'UUID',
		width: 200,
	},
	{
		field: 'title',
		headerName: 'Název',
		width: 400,
	},
	{
		field: 'model',
		headerName: 'Model',
		flex: 1,
		valueGetter: getModel,
	},
]

export const PublicationList = ({ onRowClick }: Props) => {
	const [rowCount, setRowCount] = useState<number>()
	const [publications, setPublications] = useState<Publication[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)

	useEffect(() => {
		async function fetchPublications() {
			const response = await listPublications(page, 10)

			if (response) {
				setPublications(response.results)
				setRowCount(response.total)
			}
		}
		fetchPublications()
	}, [page])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	return (
		<DataGrid
			autoHeight={true}
			columns={columns}
			pageSize={10}
			paginationMode="server"
			rowCount={rowCountState}
			rows={publications}
			rowsPerPageOptions={[]}
			onPageChange={onPageChange}
			onRowClick={onRowClick}
		/>
	)
}
