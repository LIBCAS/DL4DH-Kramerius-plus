import { Box, Grid, Paper } from '@mui/material'
import { StepExecution } from 'models'
import { JobEvent } from 'models/job/job-event'
import { JobExecution } from 'models/job/job-execution'
import { getJobEvent } from 'modules/jobs/job-api'
import { JobExecutionList } from 'components/job-event/job-execution-list'
import { FC, useEffect, useState } from 'react'
import { RouteComponentProps } from 'react-router'
import { StepExecutionList } from 'components/job-event/step-execution-list'
import { JobEventDataDisplay } from 'components/job-event/job-event-data-display'
import { StepExecutionDetail } from 'components/job-event/step-execution-detail'
import { CircularProgress } from '@material-ui/core'

export const JobEventDetailPage: FC<
	RouteComponentProps<{ jobEventId: string }>
> = ({ match }) => {
	const [lastFetched, setLastFetched] = useState<number>(Date.now())
	const [jobEvent, setJobEvent] = useState<JobEvent>()
	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()
	const [selectedStep, setSelectedStep] = useState<StepExecution>()
	const [loading, setLoading] = useState<boolean>(true)

	useEffect(() => {
		async function fetchJobDetail() {
			const jobEvent = await getJobEvent(match.params.jobEventId)
			setJobEvent(jobEvent)
		}

		fetchJobDetail()
		setSelectedExecution(undefined)
		setSelectedStep(undefined)
		setLoading(false)
	}, [match.params.jobEventId, lastFetched])

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
	)
}
