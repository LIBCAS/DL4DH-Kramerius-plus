import { Paper } from '@material-ui/core'
import { DataGrid, GridRowParams } from '@mui/x-data-grid'
import { JobEvent } from 'models/job-event'
import { dateTimeFormatter } from 'utils/formatters'

type Props = {
	jobs: JobEvent[]
	onRowClick: (params: GridRowParams) => void
}

const columns = [
	{
		field: 'id',
		headerName: 'ID',
		width: 300,
		type: 'string',
	},
	{
		field: 'created',
		headerName: 'Vytvořeno',
		width: 200,
		type: 'datetime',
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'lastExecutionStatus',
		headerName: 'Stav posledního spuštění',
		flex: 1,
	},
]

export const JobEventList = ({ jobs, onRowClick }: Props) => {
	return (
		<Paper>
			<DataGrid
				autoHeight={true}
				columns={columns}
				disableColumnFilter
				disableColumnMenu
				pageSize={10}
				rows={jobs}
				rowsPerPageOptions={[]}
				onRowClick={onRowClick}
			/>
		</Paper>
	)
}
