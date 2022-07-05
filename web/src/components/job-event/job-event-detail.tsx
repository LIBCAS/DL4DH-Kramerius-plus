import { Grid, Paper, Button, makeStyles, Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { useEffect, useState } from 'react'
import { getJobEvent, restartJobExecution } from '../../modules/jobs/job-api'
import { JobExecutionList } from './job-execution-list'
import { toast } from 'react-toastify'
import { useHistory, useParams } from 'react-router'
import { JobEvent } from 'models/job/job-event'
import { JobExecution } from 'models/job/job-execution'

const useStyles = makeStyles(() => ({
	paper: {
		padding: '20px',
	},
	button: {
		textTransform: 'none',
		padding: '6px 10px',
	},
}))

export const JobEventDetail = () => {
	const classes = useStyles()
	const { replace } = useHistory()
	const [job, setJob] = useState<JobEvent>()
	const [lastRender, setLastRender] = useState<number>(Date.now())
	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()
	const { jobId } = useParams<{ jobId: string }>()

	useEffect(() => {
		async function fetchJobDetail() {
			const jobEvent = await getJobEvent(jobId)
			setJob(jobEvent)
		}

		fetchJobDetail()
		setSelectedExecution(undefined)
	}, [jobId, lastRender])

	const handleExecutionClick = (params: number) => {
		if (job) {
			const jobExecution = job.executions.find(exec => exec.id === params)
			setSelectedExecution(jobExecution)
		}
	}

	const handleRestart = () => {
		async function doRestart() {
			if (job) {
				const response = await restartJobExecution(job.id) // show notification that restart called successfully
				if (response.ok) {
					toast('Operace proběhla úspěšně', {
						type: 'success',
					})
				}
			}
		}
		doRestart()
		setLastRender(Date.now())
	}

	const onPublicationButtonClick = () => {
		if (job) {
			replace(`/publications/${job.publicationId}`)
		}
	}

	const onRefreshClick = () => {
		setLastRender(Date.now())
	}

	return (
		<Paper className={classes.paper} elevation={4}>
			{job && (
				<Grid container direction="column" spacing={3}>
					<Grid item xs>
						<Box display="flex" flexDirection="row">
							<Box width="80%">
								<ReadOnlyField label="ID" value={'' + job?.id} />
								<ReadOnlyField label="Název úlohy" value={job?.jobName} />
								<ReadOnlyField
									label="ID Publikace"
									value={job?.publicationId}
								/>
								<ReadOnlyField
									label="Typ úlohy"
									value={job?.config.krameriusJob}
								/>
								<ReadOnlyField
									label="Parametre"
									value={Object.entries(job.config.parameters).map(
										([key, value]) => (
											<Grid key={key} container>
												<Grid item xs={4}>
													<Typography variant="body2">{key}</Typography>
												</Grid>
												<Grid item xs={8}>
													<Typography color="primary" variant="body2">
														{JSON.stringify(value)}
													</Typography>
												</Grid>
											</Grid>
										),
									)}
								/>
							</Box>
							<Box sx={{ p: 2 }}>
								<Box sx={{ p: 1 }}>
									<Button
										className={classes.button}
										color="primary"
										variant="contained"
										onClick={onPublicationButtonClick}
									>
										Zobrazit publikaci
									</Button>
								</Box>
								<Box sx={{ p: 1 }}>
									<Button
										className={classes.button}
										color="primary"
										variant="contained"
										onClick={onRefreshClick}
									>
										Znovu načíst
									</Button>
								</Box>
							</Box>
						</Box>
						<Box paddingBottom={2}>
							{job.executions.length > 0 &&
								job.executions[job.executions.length - 1].status ===
									'FAILED' && (
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
							key={lastRender}
							executions={job.executions}
							onRowClick={handleExecutionClick}
						/>
					</Grid>
				</Grid>
			)}
		</Paper>
	)
}
