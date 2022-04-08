import { Grid, Paper, Button, makeStyles } from '@material-ui/core'
import { Box } from '@mui/system'
import { GridRowParams } from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { JobExecution, JobEvent } from 'models'
import { useState } from 'react'
import { restartJobExecution } from '../job-api'
import { JobExecutionList } from '../job-execution/job-execution-list'
import { JobExecutionDetail } from '../job-execution/job-execution-detail'
import { toast } from 'react-toastify'

type Props = {
	jobEvent: JobEvent
}

const useStyles = makeStyles(() => ({
	paper: {
		padding: '20px',
	},
}))

export const JobEventDetail = ({ jobEvent: job }: Props) => {
	const classes = useStyles()

	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()

	const handleExecutionClick = (params: GridRowParams) => {
		const jobExecution = job.executions.find(
			exec => exec.id === params.row['id'],
		)
		setSelectedExecution(jobExecution)
	}

	const handleRestart = () => {
		async function doRestart() {
			const response = await restartJobExecution(job.id) // show notification that restart called successfully
			if (response.ok) {
				toast('Operace proběhla úspěšně', {
					type: 'success',
				})
			}
		}
		doRestart()
	}

	return (
		<Paper className={classes.paper} elevation={4}>
			<Grid container direction="column" spacing={3}>
				<Grid item xs>
					<Box>
						<ReadOnlyField label="ID" value={'' + job?.id} />
						<ReadOnlyField label="Název úlohy" value={job?.jobName} />
						<ReadOnlyField label="ID Publikace" value={job?.publicationId} />
						<ReadOnlyField
							label="Parametre"
							value={Object.entries(job.parameters).map(([key, value]) => (
								<ReadOnlyField
									key={key}
									label={key}
									value={JSON.stringify(value)}
								/>
							))}
						/>
					</Box>
					<Box paddingBottom={2}>
						{job.executions.length > 0 &&
							job.executions[job.executions.length - 1].status === 'FAILED' && (
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
						executions={job.executions}
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
		</Paper>
	)
}
