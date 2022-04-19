import { DataGrid, GridColDef, GridRowParams } from '@mui/x-data-grid'
import { Publication } from 'models'

type Props = {
	publications: Publication[]
	onRowClick: (params: GridRowParams) => void
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
	},
]

export const PublicationList = ({ publications, onRowClick }: Props) => {
	return (
		<DataGrid
			autoHeight={true}
			columns={columns}
			pageSize={10}
			rows={publications}
			rowsPerPageOptions={[]}
			onRowClick={onRowClick}
		/>
	)
}
