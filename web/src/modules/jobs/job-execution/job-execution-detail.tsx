import { Grid } from '@material-ui/core'
import { Box } from '@mui/system'
import { GridRowParams } from '@mui/x-data-grid'
import { useState } from 'react'
import { StepExecutionList } from '../step-execution/step-execution-list'
import { StepExecutionDetail } from '../step-execution/step-execution-detail'
import { StepExecution } from 'models'
import { JobExecution } from 'models/job/job-execution'

type Props = {
	jobExecution: JobExecution
}

export const JobExecutionDetail = ({ jobExecution }: Props) => {
	const [selectedStep, setSelectedStep] = useState<StepExecution>()

	const handleStepClick = (params: GridRowParams) => {
		const step = jobExecution.stepExecutions.find(
			step => step.stepName === params.row['stepName'],
		)
		setSelectedStep(step)
	}

	return (
		<Box p={3}>
			<Grid container direction="column" spacing={3}>
				<Grid item xs>
					<StepExecutionList
						executions={jobExecution.stepExecutions}
						onRowClick={handleStepClick}
					/>
				</Grid>
				{selectedStep && (
					<Grid item xs>
						<StepExecutionDetail stepExecution={selectedStep} />
					</Grid>
				)}
			</Grid>
		</Box>
	)
}
