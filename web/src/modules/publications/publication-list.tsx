import { DataGrid, GridColDef, GridRowParams } from '@mui/x-data-grid'
import { Publication } from 'models'
import { getPublication, getPublications } from 'modules/export/export-api'
import { useEffect, useState } from 'react'

type Props = {
	onRowClick: (params: Publication | undefined) => void
}

const columns: GridColDef[] = [
	{
		field: 'id',
		headerName: 'UUID',
		width: 300,
	},
	{
		field: 'title',
		headerName: 'Název',
		width: 500,
	},
	{
		field: 'model',
		headerName: 'Model',
		width: 100,
	},
]

export const PublicationList = ({ onRowClick }: Props) => {
	const [publications, setPublications] = useState<Publication[]>([])

	useEffect(() => {
		async function fetchPublications() {
			const response = await getPublications()
			setPublications(response)
		}
		fetchPublications()
	}, [])

	const handleClick = (params: GridRowParams) => {
		async function fetchPublication() {
			const response = await getPublication(params.row['id'])
			onRowClick(response)
		}
		fetchPublication()
	}

	return (
		<DataGrid
			autoHeight={true}
			columns={columns}
			rows={publications}
			onRowClick={handleClick}
		/>
	)
}
