import { Box, Grid, Paper } from '@mui/material'
import { StepExecution } from 'models'
import { JobEvent } from 'models/job/job-event'
import { JobExecution } from 'models/job/job-execution'
import { getJobEvent } from 'api/job-api'
import { JobExecutionList } from 'components/job-event/job-execution-list'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { StepExecutionList } from 'components/job-event/step-execution-list'
import { JobEventDataDisplay } from 'components/job-event/job-event-data-display'
import { StepExecutionDetail } from 'components/job-event/step-execution-detail'
import { CircularProgress } from '@material-ui/core'
import { JobEventRunErrorAlert } from 'modules/jobs/job-event/job-event-run-error-alert'
import { PageWrapper } from './page-wrapper'

export const JobEventDetailPage: FC = () => {
	const [lastFetched, setLastFetched] = useState<number>(Date.now())
	const [jobEvent, setJobEvent] = useState<JobEvent>()
	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()
	const [selectedStep, setSelectedStep] = useState<StepExecution>()
	const [loading, setLoading] = useState<boolean>(true)
	const { jobEventId } = useParams()

	useEffect(() => {
		async function fetchJobDetail() {
			if (jobEventId) {
				const jobEvent = await getJobEvent(jobEventId)
				setJobEvent(jobEvent)
			}
		}

		fetchJobDetail()
		setSelectedExecution(undefined)
		setSelectedStep(undefined)
		setLoading(false)
	}, [jobEventId, lastFetched])

	const handleExecutionClick = (executionId: number) => {
		setSelectedExecution(
			jobEvent?.executions.find(execution => execution.id === executionId),
		)
	}

	const handleStepClick = (stepId: number) => {
		setSelectedStep(
			selectedExecution?.stepExecutions.find(step => step.id === stepId),
		)
	}

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
							{selectedExecution && (
								<Grid item xs={12}>
									<StepExecutionList
										steps={selectedExecution?.stepExecutions}
										onRowClick={handleStepClick}
									/>
								</Grid>
							)}
							{selectedStep && (
								<Grid item xs={12}>
									<StepExecutionDetail stepExecution={selectedStep} />
								</Grid>
							)}
						</Grid>
					</Paper>
				)}
			</Box>
		</PageWrapper>
	)
}
