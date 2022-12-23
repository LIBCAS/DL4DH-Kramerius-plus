import { Box, Grid, Paper, Typography } from '@mui/material'
import { StepExecution } from 'models'
import { JobEvent } from 'models/job/job-event'
import { JobExecution } from 'models/job/job-execution'
import { getJobEvent } from 'api/job-api'
import { JobExecutionList } from 'components/job-event/job-execution-list'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { StepExecutionList } from 'components/job-event/step-execution-list'
import { JobEventDataDisplay } from 'components/job-event/job-event-data-display'
import { CircularProgress } from '@material-ui/core'
import { JobEventRunErrorAlert } from 'modules/jobs/job-event/job-event-run-error-alert'
import { PageWrapper } from './page-wrapper'

export const JobEventDetailPage: FC = () => {
	const [lastFetched, setLastFetched] = useState<number>(Date.now())
	const [jobEvent, setJobEvent] = useState<JobEvent>()
	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()
	// const [selectedStep, setSelectedStep] = useState<StepExecution>()
	const [loading, setLoading] = useState<boolean>(true)
	const { krameriusJobInstanceId } = useParams()

	useEffect(() => {
		async function fetchJobDetail() {
			if (krameriusJobInstanceId) {
				const jobEvent = await getJobEvent(krameriusJobInstanceId)
				setJobEvent(jobEvent)
			}
		}

		fetchJobDetail()
		setSelectedExecution(undefined)
		// setSelectedStep(undefined)
		setLoading(false)
	}, [krameriusJobInstanceId, lastFetched])

	const handleExecutionClick = (executionId: number) => {
		setSelectedExecution(
			jobEvent?.executions.find(execution => execution.id === executionId),
		)
		// setSelectedStep(undefined)
	}

	// const handleStepClick = (stepId: number) => {
	// 	setSelectedStep(
	// 		selectedExecution?.stepExecutions.find(step => step.id === stepId),
	// 	)
	// }

	const onRefreshClick = () => {
		setLoading(true)
		setLastFetched(Date.now())
	}

	return (
		<PageWrapper requireAuth>
			<Box
				alignContent="center"
				alignItems="center"
				display="flex"
				flexDirection="column"
				justifyContent="center"
			>
				{loading || !jobEvent ? (
					<Box m="auto">
						<CircularProgress />
					</Box>
				) : (
					<Paper sx={{ p: 5 }}>
						<Grid container spacing={2}>
							<Grid item xs={12}>
								<JobEventDataDisplay
									jobEvent={jobEvent}
									onRefreshClick={onRefreshClick}
								/>
							</Grid>
							{jobEvent.runErrorMessage && (
								<Grid item xs={12}>
									<JobEventRunErrorAlert jobEvent={jobEvent} />
								</Grid>
							)}
							<Grid item xs={12}>
								<JobExecutionList
									executions={jobEvent?.executions}
									onRowClick={handleExecutionClick}
								/>
							</Grid>
							{selectedExecution &&
								selectedExecution.stepExecutions.length > 0 && (
									<Grid item xs={12}>
										<StepExecutionList
											steps={selectedExecution?.stepExecutions}
											onRowClick={() => {}}
										/>
									</Grid>
								)}
							{/* {selectedExecution?.exitStatus.exitDescription && (
								<Grid item xs={12}>
									<Paper sx={{ p: 5 }}>
										<Grid container>
											<Grid item lg={2} xs={4}>
												<Typography variant="subtitle1">Chyba</Typography>
											</Grid>
											<Grid item lg={10} xs={8}>
												<Typography
													color="primary"
													style={{ wordWrap: 'break-word' }}
													variant="body2"
												>
													{selectedExecution.exitStatus.exitDescription}
												</Typography>
											</Grid>
										</Grid>
									</Paper>
								</Grid>
							)} */}
						</Grid>
					</Paper>
				)}
			</Box>
		</PageWrapper>
	)
}
