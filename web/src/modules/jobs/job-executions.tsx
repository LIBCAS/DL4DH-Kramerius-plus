import { Typography } from '@mui/material'
import { Box } from '@mui/system'
import { DataGrid, GridColDef, GridRowParams } from '@mui/x-data-grid'
import { JobExecution } from 'models/job/job-execution'
import { FC } from 'react'
import { dateTimeFormatter, durationFormatter } from 'utils/formatters'

const columns: GridColDef[] = [
	{
		field: 'startTime',
		headerName: 'Čas spustenia',
		width: 180,
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'endTime',
		headerName: 'Čas ukončenia',
		width: 180,
		valueFormatter: dateTimeFormatter,
	},
	{
		field: 'duration',
		headerName: 'Trvanie',
		description: '(HH:mm:ss.SSS)',
		width: 105,
		valueFormatter: durationFormatter,
	},
	{
		field: 'status',
		headerName: 'Stav',
		width: 110,
	},
	{
		field: 'exitCode',
		headerName: 'Výstup',
		flex: 1,
	},
]

export const JobExecutions: FC<{
	executions: JobExecution[]
	onExecutionClick: (executionId: number) => void
}> = ({ executions, onExecutionClick }) => {
	const onRowClick = (params: GridRowParams) => {
		onExecutionClick(params.row.id)
	}

	return (
		<Box p={1}>
			<Box pb={3}>
				<Typography variant="h6">Běhy úlohy</Typography>
			</Box>
			<Box height="220px">
				<DataGrid
					columns={columns}
					density="compact"
					disableColumnFilter
					disableColumnMenu
					getRowClassName={() => 'data-grid-row'}
					getRowId={row => row.id}
					headerHeight={50}
					hideFooter
					rowHeight={50}
					rows={executions}
					sx={{ flexGrow: 1 }}
					onRowClick={onRowClick}
				/>
			</Box>
		</Box>
	)
}
