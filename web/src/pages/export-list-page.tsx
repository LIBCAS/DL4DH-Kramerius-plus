import {
	DataGrid,
	GridColDef,
	GridValueGetterParams,
	GridRenderCellParams,
} from '@mui/x-data-grid'
import { Export } from 'models'
import { FC, FormEvent, useEffect, useState } from 'react'
import { downloadExport, listExports } from '../api/export-api'
import { Button } from '@mui/material'
import { PageWrapper } from './page-wrapper'

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
		field: 'publicationId',
		headerName: 'ID Publikace',
		width: 300,
	},
	{
		field: 'publicationTitle',
		headerName: 'Název publikace',
		width: 200,
	},
	{
		field: 'fileRefType',
		headerName: 'Typ',
		width: 150,
		valueGetter: getType,
	},
	{
		field: 'fileRefSize',
		headerName: 'Velikost',
		width: 100,
		valueGetter: getSize,
	},
	{
		field: 'action',
		headerName: 'Akce',
		flex: 1,
		sortable: false,
		renderCell: (params: GridRenderCellParams) => {
			const onClick = async (event: FormEvent) => {
				let filename = ''
				downloadExport(`${params.row['fileRef']?.id}`)
					.then(response => {
						filename =
							response.headers
								.get('content-disposition')
								?.split('; ')[1]
								.split('=')[1] ?? 'file'

						filename = filename?.substring(1, filename.length - 1)

						return response.blob()
					})
					.then(blob => {
						const url = window.URL.createObjectURL(new Blob([blob]))
						const link = document.createElement('a')
						link.href = url
						link.setAttribute('download', filename)

						document.body.appendChild(link)
						link.click()
						link.parentNode?.removeChild(link)
					})

				event.preventDefault()
			}
			return (
				<form onSubmit={onClick}>
					<Button color="primary" type="submit" variant="contained">
						Stáhnout
					</Button>
				</form>
			)
		},
	},
]

export const ExportListPage: FC = () => {
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
		<PageWrapper requireAuth>
			<DataGrid
				autoHeight={true}
				columns={columns}
				density="compact"
				pageSize={10}
				paginationMode="server"
				rowCount={rowCountState}
				rows={exports}
				rowsPerPageOptions={[10]}
				onPageChange={onPageChange}
			/>
		</PageWrapper>
	)
}
