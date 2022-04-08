import { Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { JobExecution } from 'models'
import { dateTimeFormatter, durationFormatter } from 'utils/formatters'

type Props = {
	executions: JobExecution[]
	onRowClick: (params: GridRowParams) => void
}

const getExitCode = (params: GridValueGetterParams) => {
	return params.row['exitStatus']['exitCode']
}

const executionColumns: GridColDef[] = [
	{
		field: 'status',
		headerName: 'Stav',
		width: 150,
	},
	{
		field: 'startTime',
		headerName: 'Čas spustenia',
		width: 200,
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'endTime',
		headerName: 'Čas ukončenia',
		width: 200,
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'duration',
		headerName: 'Trvanie',
		description: '(HH:mm:ss.SSS)',
		width: 120,
		valueFormatter: durationFormatter,
	},
	{
		field: 'exitStatus',
		headerName: 'Výsledný stav',
		flex: 1,
		valueGetter: getExitCode,
	},
]

export const JobExecutionList = ({ executions, onRowClick }: Props) => {
	return (
		<Box>
			<Box paddingBottom={3}>
				<Typography variant="h6">Běhy</Typography>
			</Box>
			<Box>
				<DataGrid
					autoHeight={true}
					columns={executionColumns}
					rows={executions}
					onRowClick={onRowClick}
				/>
			</Box>
		</Box>
	)
}
