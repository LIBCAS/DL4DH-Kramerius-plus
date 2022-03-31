import { Grid, Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import { GridRowParams } from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { JobExecution, StepExecution } from 'models'
import { useState } from 'react'
import { StepExecutionList } from '../step-execution/step-execution-list'
import { StepExecutionDetail } from '../step-execution/step-execution-detail'

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
					<Typography variant="h6">Zvolený běh</Typography>
				</Grid>
				<Grid item xs>
					<Box flex="true" flexDirection="row">
						<Box paddingBottom={2}>
							<Typography variant="body1">Parametre</Typography>
						</Box>
						<Box paddingBottom={2}>
							{Object.entries(jobExecution.jobParameters).map(
								([key, value]) => (
									<ReadOnlyField
										key={key}
										label={key}
										value={value.parameter as string}
									/>
								),
							)}
						</Box>
					</Box>
				</Grid>
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
