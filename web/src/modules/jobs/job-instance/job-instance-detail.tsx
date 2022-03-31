import { Grid, Paper, Button } from '@material-ui/core'
import { Box } from '@mui/system'
import { GridRowParams } from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { JobExecution, JobInstance } from 'models'
import { useEffect, useState } from 'react'
import { getJobExecutions, restartJobExecution } from '../job-api'
import { JobExecutionList } from '../job-execution/job-execution-list'
import { JobExecutionDetail } from '../job-execution/job-execution-detail'
import { toast } from 'react-toastify'

type Props = {
	job: JobInstance
}

export const JobInstanceDetail = ({ job }: Props) => {
	const [jobExecutions, setJobExecutions] = useState<JobExecution[]>([])
	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()
	const [lastExecution, setLastExecution] = useState<JobExecution>()

	async function fetchExecutions() {
		const response = await getJobExecutions('' + job.id)
		setJobExecutions(response)
		setSelectedExecution(undefined)
	}

	useEffect(() => {
		fetchExecutions()
	}, [job])

	useEffect(() => {
		if (jobExecutions.length > 0) {
			setLastExecution(jobExecutions[jobExecutions.length - 1])
		}
		console.log('Setting selected execution to undefined')
	}, [jobExecutions])

	const handleExecutionClick = (params: GridRowParams) => {
		const job = jobExecutions.find(exec => exec.id === params.row['id'])
		setSelectedExecution(job)
	}

	const handleRestart = () => {
		async function doRestart() {
			if (lastExecution) {
				const response = await restartJobExecution(lastExecution.id) // show notification that restart called successfully
				if (response.ok) {
					toast('Operace proběhla úspěšně', {
						type: 'success',
					})
				}
			}
		}
		doRestart()
		fetchExecutions()
	}

	console.log(lastExecution)

	return (
		<Grid container direction="column" spacing={3}>
			<Grid item xs>
				<Box>
					<ReadOnlyField label="ID" value={'' + job?.id} />
					<ReadOnlyField label="Název úlohy" value={job?.jobName} />
				</Box>
				<Box paddingBottom={2}>
					{lastExecution && lastExecution.status === 'FAILED' && (
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
			</Grid>
			<Grid item xs>
				<JobExecutionList
					executions={jobExecutions}
					onRowClick={handleExecutionClick}
				/>
			</Grid>
			{selectedExecution && (
				<Grid item xs>
					<Paper elevation={3}>
						<JobExecutionDetail jobExecution={selectedExecution} />
					</Paper>
				</Grid>
			)}
		</Grid>
	)
}
