import { Grid, Typography, Divider, Button } from '@mui/material'
import { JobEvent } from 'models/job/job-event'
import { FC } from 'react'
import { Link } from 'react-router-dom'
import { formatDateTime } from 'utils/formatters'
import { JobEventDataRow } from './job-event-data-row'

type Props = {
	jobEvent: JobEvent
	onRefreshClick: () => void
}

export const JobEventDataDisplay: FC<Props> = ({
	jobEvent,
	onRefreshClick,
}) => {
	return (
		<Grid container direction="column" spacing={2} sx={{ p: 2 }}>
			<Grid container justifyContent="space-between">
				<Grid item xs={6}>
					<Typography color="text.primary" variant="h6">
						Detail úlohy
					</Typography>
				</Grid>
				<Grid container item spacing={2} xs={4}>
					<Grid item lg={6} md={12}>
						<Link
							style={{ textDecoration: 'none ' }}
							to={`/publications/${jobEvent.publicationId}`}
						>
							<Button color="primary" fullWidth variant="contained">
								Zobrazit publikaci
							</Button>
						</Link>
					</Grid>
					<Grid item lg={6} md={12}>
						<Button
							color="primary"
							fullWidth
							variant="contained"
							onClick={onRefreshClick}
						>
							Znovu načíst
						</Button>
					</Grid>
				</Grid>
			</Grid>
			<Grid item>
				<Divider />
			</Grid>
			<Grid container spacing={1} sx={{ p: 2 }}>
				<JobEventDataRow label="ID" value={jobEvent.id} />
				<JobEventDataRow
					label="Úloha vytvořená"
					value={formatDateTime(jobEvent.created.toString())}
				/>
				<JobEventDataRow label="Název úlohy" value={jobEvent.jobName || ''} />
				<JobEventDataRow
					label="UUID Publikace"
					value={jobEvent.publicationId}
				/>
				<JobEventDataRow
					label="Typ úlohy"
					value={jobEvent.config.krameriusJob}
				/>
				<JobEventDataRow
					label="Parametre úlohy (JSON)"
					value={JSON.stringify(jobEvent.config.parameters, null, 4)}
					valueComponent="pre"
				/>
			</Grid>
		</Grid>
	)
}
