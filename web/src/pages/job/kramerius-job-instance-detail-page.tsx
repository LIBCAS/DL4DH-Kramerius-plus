import {
	Box,
	Button,
	CircularProgress,
	Dialog,
	DialogContent,
	DialogTitle,
	Divider,
	Grid,
	Paper,
	Typography,
} from '@mui/material'
import { styled } from '@mui/system'
import { getJob, getJobEvent } from 'api/job-api'
import { StepExecution } from 'models'
import { JobExecution } from 'models/job/job-execution'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { JobExecutions } from 'modules/jobs/job-executions'
import { JobInfo } from 'modules/jobs/job-info'
import { JobParameters } from 'modules/jobs/job-parameters'
import { StepExecutions } from 'modules/jobs/step-executions'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router'

export const KrameriusJobInstanceDetailPage: FC = () => {
	const [job, setJob] = useState<KrameriusJobInstance>()
	const [jobExecution, setJobExecution] = useState<JobExecution>()
	const [stepExecution, setStepExecution] = useState<StepExecution>()
	const [loading, setLoading] = useState<boolean>(true)
	const { krameriusJobInstanceId } = useParams()

	useEffect(() => {
		const fetchJob = async () => {
			if (krameriusJobInstanceId) {
				const job = await getJob(krameriusJobInstanceId)
				setJob(job)
			}
		}

		fetchJob()
		setLoading(false)
	}, [])

	const onJobExecutionClick = (executionId: number) => {
		const jobExecution = job?.executions.find(exec => exec.id === executionId)
		setJobExecution(jobExecution)
	}

	const onStepExecutionClick = (executionId: number) => {
		const stepExecution = jobExecution?.stepExecutions.find(
			exec => exec.id === executionId,
		)
		setStepExecution(stepExecution)
	}

	const onDialogClose = () => {
		setStepExecution(undefined)
	}

	return (
		<PageWrapper requireAuth>
			{loading || !job ? (
				<Box
					alignContent="center"
					alignItems="center"
					display="flex"
					flexDirection="column"
					justifyContent="center"
				>
					<Box m="auto">
						<CircularProgress />
					</Box>
				</Box>
			) : (
				<Box>
					<Box display="flex" justifyContent="space-between" p={2} pb={3}>
						<Box>
							<Typography variant="h4">Detail úlohy</Typography>
						</Box>
						<Box display="flex" justifyContent="space-between" width="20%">
							<Box width="45%">
								<Button fullWidth variant="contained">
									Restartovat
								</Button>
							</Box>
							<Box width="10%"></Box>
							<Box width="45%">
								<Button fullWidth variant="contained">
									Zastavit
								</Button>
							</Box>
						</Box>
					</Box>
					<Grid container spacing={1}>
						<Grid item sx={{ p: 2 }} xs={2.7}>
							<Paper elevation={4} sx={{ height: 300, p: 2 }}>
								<JobInfo job={job} />
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={4}>
							<Paper elevation={4} sx={{ height: 300, p: 2 }}>
								<JobParameters jobParameters={job.jobParameters} />
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={5.3}>
							<Paper elevation={4} sx={{ height: 300, p: 2 }}>
								<JobExecutions
									executions={job.executions}
									onExecutionClick={onJobExecutionClick}
								/>
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={12}>
							<Paper elevation={4} sx={{ height: 370, p: 2 }}>
								<StepExecutions
									executions={jobExecution?.stepExecutions}
									onStepExecutionClick={onStepExecutionClick}
								/>
							</Paper>
						</Grid>
					</Grid>
					<Dialog maxWidth="xl" open={!!stepExecution} onClose={onDialogClose}>
						<DialogTitle>Chyby v zvoleném kroku</DialogTitle>
						<DialogContent>
							{stepExecution?.errors.map((error, i) => (
								<Grid key={i} container item spacing={2}>
									<Grid item xs={12}>
										<Divider />
									</Grid>
									<Grid item xs={1}>
										<Typography variant="body1">{i + 1}.</Typography>
									</Grid>
									<Grid item xs={11}>
										<Typography color="primary" variant="body1">
											{error.exitCode}
										</Typography>
									</Grid>
									<Grid item xs={1}>
										<Typography variant="body1">Stacktrace:</Typography>
									</Grid>
									<Grid item xs={11}>
										<Typography color="primary" variant="body1">
											{error.stackTrace}
										</Typography>
									</Grid>
								</Grid>
							))}
						</DialogContent>
					</Dialog>
				</Box>
			)}
		</PageWrapper>
	)
}
