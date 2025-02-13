import { Typography, Box } from '@mui/material'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { JobExecution } from 'models/job/job-execution'
import { FC } from 'react'
import { dateTimeFormatter, durationFormatter } from 'utils/formatters'

type Props = {
	executions: JobExecution[]
	onRowClick: (executionId: number) => void
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
		field: 'createTime',
		headerName: 'Čas vytvorenia',
		width: 200,
		valueFormatter: dateTimeFormatter,
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

export const JobExecutionList: FC<Props> = ({
	executions,
	onRowClick,
}: Props) => {
	const handleExecutionClick = (params: GridRowParams) => {
		onRowClick(params.row['id'])
	}

	return (
		<Box>
			<Box paddingBottom={3}>
				<Typography variant="h6">Běhy</Typography>
			</Box>
			<Box>
				<DataGrid
					autoHeight={true}
					columns={executionColumns}
					density="compact"
					pageSizeOptions={[]}
					rows={executions}
					onRowClick={handleExecutionClick}
				/>
			</Box>
		</Box>
	)
}
