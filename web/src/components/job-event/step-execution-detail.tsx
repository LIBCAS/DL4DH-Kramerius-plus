import { Grid, Typography } from '@mui/material'
import { StepExecution } from 'models'
import { FC } from 'react'

type Props = {
	stepExecution: StepExecution
}

export const StepExecutionDetail: FC<Props> = ({ stepExecution }) => {
	return (
		<Grid container justifyItems="flex-start" spacing={3}>
			<Grid item xs={12}>
				<Typography variant="h6">Zvolený krok</Typography>
			</Grid>
			<Grid container item xs={12}>
				<Grid container spacing={1}>
					{/* <Grid item lg={2} xs={4}>
						<Typography variant="body2">Konečný stav</Typography>
					</Grid>
					<Grid item lg={10} xs={8}>
						<Typography color="primary" variant="body2">
							{stepExecution.exitStatus.exitCode}
						</Typography>
					</Grid>
					{stepExecution.exitStatus.exitDescription && (
						<>
							<Grid item lg={2} xs={4}>
								<Typography variant="body2">Chyba</Typography>
							</Grid>
							<Grid item lg={10} xs={8}>
								<Typography
									color="primary"
									style={{ wordWrap: 'break-word' }}
									variant="body2"
								>
									{stepExecution.exitStatus.exitDescription}
								</Typography>
							</Grid>
						</>
					)} */}
				</Grid>
			</Grid>
		</Grid>
	)
}
