import {
	Box,
	Button,
	Dialog,
	DialogContent,
	DialogTitle,
	Divider,
	Grid,
	Paper,
	Typography,
} from '@mui/material'
import { getJob, restartJob, stopJob } from 'api/job-api'
import { Loading } from 'components/loading'
import { ApiError, StepExecution } from 'models'
import { JobExecution } from 'models/job/job-execution'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { JobExecutions } from 'modules/jobs/job-executions'
import { JobInfo } from 'modules/jobs/job-info'
import { JobParameters } from 'modules/jobs/job-parameters'
import { StepExecutions } from 'modules/jobs/step-executions'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router'
import { toast } from 'react-toastify'

export const KrameriusJobInstanceDetailPage: FC = () => {
	const [job, setJob] = useState<KrameriusJobInstance>()
	const [jobExecution, setJobExecution] = useState<JobExecution>()
	const [stepExecution, setStepExecution] = useState<StepExecution>()
	const [loading, setLoading] = useState<boolean>(true)
	const { krameriusJobInstanceId } = useParams()

	const navigate = useNavigate()

	const onPublicationClick = () => {
		navigate(`/publications/${job?.jobParameters['PUBLICATION_ID']}`)
	}

	const fetchJob = async () => {
		if (krameriusJobInstanceId) {
			const job = await getJob(krameriusJobInstanceId)
			setJob(job)
		}
	}

	useEffect(() => {
		fetchJob()
		setLoading(false)
		// eslint-disable-next-line react-hooks/exhaustive-deps
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

	const isRestartable = (): boolean => {
		return ['FAILED_FATALLY', 'STOPPED', 'FAILED'].includes(
			job ? job.executionStatus : '',
		)
	}

	const reload = () => {
		fetchJob()
		toast('Informace byli aktualizovány.', { type: 'info' })
	}

	const onRestart = () => {
		const callRestart = async () => {
			const response = await restartJob(job!.id)

			if (response.ok) {
				toast('Úloha úspěšně restartována', {
					type: 'success',
				})
			} else {
				const body = await response.json()
				toast(`Nastala chyba: ${(body as ApiError).message}`, {
					type: 'error',
				})
			}

			fetchJob()
		}

		callRestart()
	}

	const isStoppable = () => {
		return job?.executionStatus === 'STARTED'
	}

	const onStop = () => {
		const callStop = async () => {
			const response = await stopJob(job!.id)

			if (response.ok) {
				toast('Úloha úspěšně zastavena', {
					type: 'success',
				})
			} else {
				const body = await response.json()
				toast(`Nastala chyba: ${(body as ApiError).message}`, {
					type: 'error',
				})
			}

			fetchJob()
		}

		callStop()
	}

	return (
		<PageWrapper requireAuth>
			{loading || !job ? (
				<Loading />
			) : (
				<Box>
					<Grid container spacing={1}>
						<Grid container item spacing={2} sx={{ p: 2 }} xs={12}>
							<Grid item lg={5} md={3} xl={7} xs={12}>
								<Typography variant="h4">Detail úlohy</Typography>
							</Grid>
							<Grid item lg={2} md={2.5} xl={1.5} xs={12}>
								<Button fullWidth variant="contained" onClick={reload}>
									Znovu načíst
								</Button>
							</Grid>
							<Grid item lg={2} md={2.5} xl={1.5} xs={12}>
								<Button
									disabled={!job.jobParameters['PUBLICATION_ID']}
									fullWidth
									variant="contained"
									onClick={onPublicationClick}
								>
									Zobrazit publikaci
								</Button>
							</Grid>
							<Grid item lg={1.5} md={2} xl={1} xs={12}>
								<Button
									disabled={!isRestartable()}
									fullWidth
									variant="contained"
									onClick={onRestart}
								>
									Restartovat
								</Button>
							</Grid>
							<Grid item lg={1.5} md={2} xl={1} xs={12}>
								<Button
									disabled={!isStoppable()}
									fullWidth
									variant="contained"
									onClick={onStop}
								>
									Zastavit
								</Button>
							</Grid>
						</Grid>
						<Grid container item spacing={1} xs={12}>
							<Grid item md={6} sx={{ p: 2 }} xl={2.7} xs={12}>
								<Paper elevation={4} sx={{ height: 300, p: 2 }}>
									<JobInfo job={job} />
								</Paper>
							</Grid>
							<Grid item md={6} sx={{ p: 2 }} xl={4} xs={12}>
								<Paper elevation={4} sx={{ height: 300, p: 2 }}>
									<JobParameters jobParameters={job.jobParameters} />
								</Paper>
							</Grid>
							<Grid item md={12} sx={{ p: 2 }} xl={5.3} xs={12}>
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
					</Grid>
					<Dialog maxWidth="xl" open={!!stepExecution} onClose={onDialogClose}>
						<DialogTitle>Chyby ve vybraném kroku</DialogTitle>
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
											<pre>{error.stackTrace}</pre>
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
