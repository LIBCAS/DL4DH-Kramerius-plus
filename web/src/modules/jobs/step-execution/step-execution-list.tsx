import { Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { StepExecution } from 'models'

type Props = {
	executions: StepExecution[]
	onRowClick: (params: GridRowParams) => void
}

const dateTimeFormatter = (params: GridValueFormatterParams) => {
	if (params.value === undefined) {
		return undefined
	}

	const date = new Date(params.value as string)
	return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}:${date.getMilliseconds()}`
}

const durationFormatter = (params: GridValueFormatterParams) => {
	const durationInMillis = params.value as number
	if (durationInMillis === undefined) {
		return undefined
	}

	return new Date(durationInMillis).toISOString().slice(11, -1)
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
				<Typography variant="h6">Vykonané kroky</Typography>
			</Box>
			<Box paddingBottom={3}>
				<DataGrid
					autoHeight={true}
					columns={stepColumns}
					getRowId={r => r.stepName}
					rows={executions}
					onRowClick={onRowClick}
				/>
			</Box>
		</Box>
	)
}
