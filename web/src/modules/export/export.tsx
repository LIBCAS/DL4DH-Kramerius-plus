import {
	DataGrid,
	GridColDef,
	GridValueGetterParams,
	GridRenderCellParams,
} from '@mui/x-data-grid'
import { Export } from 'models'
import { useEffect, useState } from 'react'
import { listExports } from './export-api'
import { Button } from '@material-ui/core'

const getType = (params: GridValueGetterParams) => {
	return params.row['fileRef'].contentType
}

const getDate = (params: GridValueGetterParams) => {
	const created = params.row['created']
	return created ? new Date(created).toLocaleString('cs') : undefined
}

const getSize = (params: GridValueGetterParams) => {
	const size = params.row['fileRef'].size
	return `${((size ?? 0) / 1048576).toFixed(2)} MB`
}

const getId = (params: GridValueGetterParams) => {
	return params.row['fileRef'].id
}

const columns: GridColDef[] = [
	{
		field: 'id',
		headerName: 'ID Exportu',
		width: 300,
	},
	{
		field: 'created',
		headerName: 'Vytvořeno',
		width: 200,
		valueGetter: getDate,
	},
	{
		field: 'fileRefId',
		headerName: 'ID Publikace',
		width: 300,
		valueGetter: getId,
	},
	{
		field: 'publicationTitle',
		headerName: 'Název publikace',
		width: 400,
	},
	{
		field: 'fileRefType',
		headerName: 'Typ',
		width: 200,
		valueGetter: getType,
	},
	{
		field: 'fileRefSize',
		headerName: 'Velikost',
		width: 200,
		valueGetter: getSize,
	},
	{
		field: 'action',
		headerName: 'Akce',
		flex: 1,
		sortable: false,
		renderCell: (params: GridRenderCellParams) => {
			const onClick = () => {
				window.open(
					process.env.PUBLIC_URL +
						`/api/exports/download/${params.row['fileRef']?.id}`,
					'_blank',
				)
			}
			return (
				<Button color="primary" variant="contained" onClick={onClick}>
					Stáhnout
				</Button>
			)
		},
	},
]

export const ExportList = () => {
	const [rowCount, setRowCount] = useState<number>()
	const [exports, setExports] = useState<Export[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)

	useEffect(() => {
		async function fetchExports() {
			const response = await listExports(page, 10)

			if (response) {
				setExports(response.results)
				setRowCount(response.total)
			}
		}
		fetchExports()
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
			rows={exports}
			rowsPerPageOptions={[]}
			onPageChange={onPageChange}
		/>
	)
}
