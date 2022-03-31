import { Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { JobExecution } from 'models'

type Props = {
	executions: JobExecution[]
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

const executionColumns: GridColDef[] = [
	{
		field: 'id',
		headerName: 'ID',
		width: 90,
		type: 'number',
	},
	{
		field: 'status',
		headerName: 'Stav',
		width: 150,
	},
	{
		field: 'startTime',
		headerName: 'Čas spustenia',
		width: 220,
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'endTime',
		headerName: 'Čas ukončenia',
		width: 220,
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'duration',
		headerName: 'Trvanie (HH:mm:ss.SSS)',
		width: 200,
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
