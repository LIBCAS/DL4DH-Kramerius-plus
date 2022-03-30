import { Grid, Typography } from '@material-ui/core'
import { Box } from '@mui/system'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { StepExecution } from 'models'

type Props = {
	stepExecution: StepExecution
}

export const StepExecutionDetail = ({ stepExecution }: Props) => {
	return (
		<Box justifyItems="flex-start">
			<Box paddingBottom={3}>
				<Typography variant="h6">Zvolený krok</Typography>
			</Box>
			<Box>
				<Grid container direction="column">
					<ReadOnlyField
						label="Konečný stav"
						value={stepExecution.exitStatus.exitCode}
					/>
					{stepExecution.exitStatus.exitDescription && (
						<ReadOnlyField
							label="Chyba"
							value={stepExecution.exitStatus.exitDescription}
						/>
					)}
				</Grid>
			</Box>
		</Box>
	)
}
