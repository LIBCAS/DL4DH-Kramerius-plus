import { Alert, AlertTitle, Grid, Typography } from '@mui/material'
import { JobEvent } from 'models/job/job-event'
import { FC } from 'react'

type Props = {
	jobEvent: JobEvent
}

export const JobEventRunErrorAlert: FC<Props> = ({ jobEvent }) => {
	return (
		<Alert severity="error" variant="filled">
			<AlertTitle>Chyba při spouštění úlohy!</AlertTitle>
			<Grid container>
				<Grid item xs={2}>
					<Typography display="inline" fontWeight={600} variant="body2">
						Chybová hláška:
					</Typography>
				</Grid>
				<Grid item xs={10}>
					<Typography display="inline" variant="body2">
						{' ' + jobEvent.runErrorMessage}
					</Typography>
				</Grid>
				<Grid item xs={2}>
					<Typography fontWeight={600} variant="body2">
						StackTrace:
					</Typography>
				</Grid>
				<Grid item xs={10}>
					<Typography
						style={{
							wordWrap: 'break-word',
							whiteSpace: 'pre-line',
						}}
						variant="body2"
					>
						{jobEvent.runErrorStackTrace}
					</Typography>
				</Grid>
			</Grid>
		</Alert>
	)
}
