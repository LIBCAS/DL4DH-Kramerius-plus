import { Grid } from '@material-ui/core'
import { FC } from 'react'
import { JobExecution } from 'models/job/job-execution'
import { StepExecutionList } from 'components/job-event/step-execution-list'

type Props = {
	jobExecution: JobExecution
	onStepClick: (stepId: number) => void
}

export const JobExecutionDetailL: FC<Props> = ({
	jobExecution,
	onStepClick,
}) => {
	return (
		<Grid container direction="column" spacing={3}>
			<Grid item xs>
				<StepExecutionList
					steps={jobExecution.stepExecutions}
					onRowClick={onStepClick}
				/>
			</Grid>
		</Grid>
	)
}
