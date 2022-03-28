import { Grid, Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import {
	DataGrid,
	GridColDef,
	GridRowParams,
	GridValueFormatterParams,
	GridValueGetterParams,
} from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { JobExecution, JobInstance } from 'models'
import { useEffect, useState } from 'react'
import { getJobExecutions } from '../job-api'
import { JobExecutionDetail } from './job-execution-detail'

type Props = {
	job: JobInstance
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

export const JobInstanceDetail = ({ job }: Props) => {
	const [jobExecutions, setJobExecutions] = useState<JobExecution[]>([])
	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()

	useEffect(() => {
		async function fetchExecutions() {
			const response = await getJobExecutions('' + job.id)
			setJobExecutions(response)
		}
		fetchExecutions()
	}, [job])

	const handleExecutionClick = (params: GridRowParams) => {
		const job = jobExecutions.find(exec => exec.id === params.row['id'])
		setSelectedExecution(job)
	}

	return (
		<Grid container direction="column" spacing={3}>
			<Grid item xs>
				<Box>
					<ReadOnlyField label="ID" value={'' + job?.id} />
					<ReadOnlyField label="Název úlohy" value={job?.jobName} />
				</Box>
			</Grid>
			<Grid item xs>
				<Typography variant="h6">Spustenia</Typography>
			</Grid>
			<Grid item xs>
				<DataGrid
					autoHeight={true}
					columns={executionColumns}
					rows={jobExecutions}
					onRowClick={handleExecutionClick}
				/>
			</Grid>
			{selectedExecution && (
				<Grid item xs>
					<JobExecutionDetail jobExecution={selectedExecution} />
				</Grid>
			)}
		</Grid>
	)
}
