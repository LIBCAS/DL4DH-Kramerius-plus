import { Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { StepExecution } from 'models'
import { dateTimeFormatter, durationFormatter } from 'utils/formatters'

type Props = {
	executions: StepExecution[]
	onRowClick: (params: GridRowParams) => void
}

const getExitCode = (params: GridValueGetterParams) => {
	return params.row['exitStatus']['exitCode']
}

const stepColumns: GridColDef[] = [
	{
		field: 'stepName',
		headerName: 'Krok',
		width: 300,
	},
	{
		field: 'status',
		headerName: 'Stav',
		width: 150,
	},
	{
		field: 'startTime',
		headerName: 'Čas spustenia',
		width: 200,
		type: 'datetime',
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
		headerName: 'Trvanie (HH:mm:ss.SSS)',
		width: 150,
		valueFormatter: durationFormatter,
	},
	{
		field: 'exitStatus',
		headerName: 'Výsledný stav',
		flex: 1,
		valueGetter: getExitCode,
	},
]

export const StepExecutionList = ({ executions, onRowClick }: Props) => {
	return (
		<Box>
			<Box paddingBottom={3}>
				<Typography variant="h6">Vykonané kroky v zvolenom běhu</Typography>
			</Box>
			<Box paddingBottom={3}>
				<DataGrid
					autoHeight={true}
					columns={stepColumns}
					getRowId={r => r.stepName}
					rows={executions}
					rowsPerPageOptions={[]}
					onRowClick={onRowClick}
				/>
			</Box>
		</Box>
	)
}
