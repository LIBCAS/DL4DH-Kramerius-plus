import { Typography, Box } from '@mui/material'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { StepExecution } from 'models'
import { FC } from 'react'
import { dateTimeFormatter, durationFormatter } from 'utils/formatters'

type Props = {
	steps: StepExecution[]
	onRowClick: (stepId: number) => void
}

const getExitCode = (params: GridValueGetterParams) => {
	return params.row['exitStatus']['exitCode']
}

const stepColumns: GridColDef[] = [
	{
		field: 'stepName',
		headerName: 'Krok',
		width: 210,
	},
	{
		field: 'status',
		headerName: 'Stav',
		width: 130,
	},
	{
		field: 'startTime',
		headerName: 'Čas spustenia',
		width: 200,
		type: 'string',
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

export const StepExecutionList: FC<Props> = ({ steps, onRowClick }) => {
	const handleStepClick = (params: GridRowParams) => {
		onRowClick(params.row['id'])
	}
	return (
		<Box>
			<Box sx={{ paddingBottom: 2 }}>
				<Typography variant="h6">Vykonané kroky v zvolenom běhu</Typography>
			</Box>
			<Box>
				<DataGrid
					autoHeight={true}
					columns={stepColumns}
					density="compact"
					rows={steps}
					rowsPerPageOptions={[]}
					onRowClick={handleStepClick}
				/>
			</Box>
		</Box>
	)
}
