import {
	DataGrid,
	GridCallbackDetails,
	GridColDef,
	GridRowParams,
	GridSelectionModel,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { Publication } from 'models'
import { FC } from 'react'
import { dateTimeFormatter } from 'utils/formatters'

type Props = {
	publications: Publication[]
	rowCount?: number
	onPageChange: (page: number) => void
	onRowClick: (publicationId: string) => void
	onSelectionChange: (
		selectionModel: GridSelectionModel,
		details: GridCallbackDetails,
	) => void
}

const getModel = (params: GridValueGetterParams) => {
	return params.row['model']
}

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
		width: 200,
	},
	{
		field: 'model',
		headerName: 'Model',
		flex: 1,
		valueGetter: getModel,
	},
]

export const PublicationListTable: FC<Props> = ({
	publications,
	rowCount,
	onPageChange,
	onRowClick,
	onSelectionChange,
}) => {
	const handleRowClick = (params: GridRowParams) => {
		onRowClick(params.row['id'])
	}

	return (
		<DataGrid
			autoHeight={true}
			checkboxSelection
			columns={columns}
			density="compact"
			disableColumnFilter
			disableColumnMenu
			pageSize={10}
			paginationMode="server"
			rowCount={rowCount}
			rows={publications}
			rowsPerPageOptions={[]}
			onPageChange={onPageChange}
			onRowClick={handleRowClick}
			onSelectionModelChange={onSelectionChange}
		/>
	)
}
