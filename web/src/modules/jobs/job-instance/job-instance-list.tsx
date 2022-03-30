import { Paper } from '@material-ui/core'
import { DataGrid, GridRowParams } from '@mui/x-data-grid'
import { JobInstance } from 'models/job-instance'

type Props = {
	jobs: JobInstance[]
	onRowClick: (params: GridRowParams) => void
}

const columns = [
	{
		field: 'id',
		headerName: 'ID',
		width: 90,
		type: 'number',
	},
	{
		field: 'jobName',
		headerName: 'Název úlohy',
		flex: 1,
	},
]

export const JobList = ({ jobs, onRowClick }: Props) => {
	return (
		<Paper>
			<DataGrid
				autoHeight={true}
				columns={columns}
				disableColumnFilter
				disableColumnMenu
				rows={jobs}
				rowsPerPageOptions={[]}
				onRowClick={onRowClick}
			/>
		</Paper>
	)
}
