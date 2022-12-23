import { Grid, Typography, Divider, Button } from '@mui/material'
import { restartJob, stopJob } from 'api/job-api'
import { ApiError } from 'models'
import { JobEvent } from 'models/job/job-event'
import { FC, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { toast } from 'react-toastify'
import { formatDateTime } from 'utils/formatters'
import { DataRow } from './data-row'

type Props = {
	jobEvent: JobEvent
	onRefreshClick: () => void
}

export const JobEventDataDisplay: FC<Props> = ({
	jobEvent,
	onRefreshClick,
}) => {
	const onStop = () => {
		const callStop = async () => {
			const response = await stopJob(jobEvent.id)

			if (response.ok) {
				toast('Úloha úspěšně zastavená', {
					type: 'success',
				})
			} else {
				const body = await response.json()
				toast(`Nastala chyba: ${(body as ApiError).message}`, {
					type: 'error',
				})
			}

			onRefreshClick()
		}

		callStop()
	}

	useEffect(() => {
		// const decodedJson = JSON.parse(jobEvent.config.parameters)
		console.log(JSON.stringify(jobEvent.config.parameters))
	})

	const onRestart = () => {
		const callRestart = async () => {
			const response = await restartJob(jobEvent.id)

			if (response.ok) {
				toast('Úloha úspěšně restartována', {
					type: 'success',
				})
			} else {
				const body = await response.json()
				toast(`Nastala chyba: ${(body as ApiError).message}`, {
					type: 'error',
				})
			}

			onRefreshClick()
		}
		callRestart()
	}

	return (
		<Grid container direction="column" spacing={2} sx={{ p: 2 }}>
			<Grid container justifyContent="space-between">
				<Grid item xs={6}>
					<Typography color="text.primary" variant="h6">
						Detail úlohy
					</Typography>
				</Grid>
				<Grid container item spacing={2} xs={6}>
					<Grid item lg={3} md={12}>
						<Button
							color="primary"
							fullWidth
							variant="contained"
							onClick={onStop}
						>
							Zastavit
						</Button>
					</Grid>
					<Grid item lg={3} md={12}>
						<Button
							color="primary"
							fullWidth
							variant="contained"
							onClick={onRestart}
						>
							Restartovat
						</Button>
					</Grid>
					<Grid item lg={3} md={12}>
						<Link
							style={{ textDecoration: 'none ' }}
							to={`/publications/${jobEvent.publicationId}`}
						>
							<Button color="primary" fullWidth variant="contained">
								Zobrazit publikaci
							</Button>
						</Link>
					</Grid>
					<Grid item lg={3} md={12}>
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
				<DataRow label="ID" value={jobEvent.id} />
				<DataRow
					label="Úloha vytvořená"
					value={formatDateTime(jobEvent.created.toString())}
				/>
				<DataRow label="UUID Publikace" value={jobEvent.publicationId} />
				<DataRow label="Typ úlohy" value={jobEvent.config.krameriusJob} />
				<DataRow
					label="Parametre úlohy (JSON)"
					value={JSON.stringify(jobEvent.config.parameters, null, 4)}
				/>
			</Grid>
		</Grid>
	)
}
