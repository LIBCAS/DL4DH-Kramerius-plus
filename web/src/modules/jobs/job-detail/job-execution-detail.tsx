import { Grid, Typography, Button } from '@material-ui/core'
import { Box } from '@mui/system'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { JobExecution, StepExecution } from 'models'
import { useState } from 'react'
import { restartJobExecution } from '../job-api'
import { StepExecutionDetail } from './step-execution-detail'

type Props = {
	jobExecution: JobExecution
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

export const JobExecutionDetail = ({ jobExecution }: Props) => {
	const [selectedStep, setSelectedStep] = useState<StepExecution>()

	const handleStepClick = (params: GridRowParams) => {
		const step = jobExecution.stepExecutions.find(
			step => step.stepName === params.row['stepName'],
		)
		setSelectedStep(step)
	}

	const handleRestart = () => {
		async function doRestart() {
			await restartJobExecution(jobExecution?.id)
		}
		doRestart()
	}

	return (
		<Grid container direction="column" spacing={3}>
			<Grid item xs>
				<Typography variant="h6">Zvolené spustenie</Typography>
			</Grid>
			<Grid item xs>
				<Box flex="true" flexDirection="row">
					<Box>
						{Object.entries(jobExecution.jobParameters).map(([key, value]) => (
							<ReadOnlyField
								key={key}
								label={key}
								value={value.parameter as string}
							/>
						))}
					</Box>
					<Box>
						{jobExecution.exitStatus.exitCode === 'FAILED' && (
							<Button
								color="primary"
								type="submit"
								variant="contained"
								onClick={handleRestart}
							>
								Restartovat
							</Button>
						)}
					</Box>
				</Box>
			</Grid>
			<Grid item xs>
				<Typography variant="h6">Vykonané kroky</Typography>
			</Grid>
			<Grid item xs>
				<DataGrid
					autoHeight={true}
					columns={stepColumns}
					getRowId={r => r.stepName}
					rows={jobExecution.stepExecutions}
					onRowClick={handleStepClick}
				/>
			</Grid>
			{selectedStep && (
				<Grid item xs>
					<StepExecutionDetail stepExecution={selectedStep} />
				</Grid>
			)}
		</Grid>
	)
}
