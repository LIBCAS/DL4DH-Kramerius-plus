import { Grid, Paper, Button, makeStyles } from '@material-ui/core'
import { Box } from '@mui/system'
import { GridRowParams } from '@mui/x-data-grid'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { JobExecution, JobEvent } from 'models'
import { useState } from 'react'
import { restartJobExecution } from '../job-api'
import { JobExecutionList } from '../job-execution/job-execution-list'
import { JobExecutionDetail } from '../job-execution/job-execution-detail'
import { toast } from 'react-toastify'
import { useHistory } from 'react-router'

type Props = {
	jobEvent: JobEvent
}

const useStyles = makeStyles(() => ({
	paper: {
		padding: '20px',
	},
	button: {
		textTransform: 'none',
		padding: '6px 10px',
	},
}))

export const JobEventDetail = ({ jobEvent }: Props) => {
	const classes = useStyles()
	const { replace } = useHistory()

	const [selectedExecution, setSelectedExecution] = useState<JobExecution>()

	const handleExecutionClick = (params: GridRowParams) => {
		const jobExecution = jobEvent.executions.find(
			exec => exec.id === params.row['id'],
		)
		setSelectedExecution(jobExecution)
	}

	const handleRestart = () => {
		async function doRestart() {
			const response = await restartJobExecution(jobEvent.id) // show notification that restart called successfully
			if (response.ok) {
				toast('Operace proběhla úspěšně', {
					type: 'success',
				})
			}
		}
		doRestart()
	}

	const onPublicationButtonClick = () => {
		replace(`/publications/${jobEvent.publicationId}`)
	}

	return (
		<Paper className={classes.paper} elevation={4}>
			<Grid container direction="column" spacing={3}>
				<Grid item xs>
					<Box display="flex" flexDirection="row">
						<Box width="80%">
							<ReadOnlyField label="ID" value={'' + jobEvent?.id} />
							<ReadOnlyField label="Název úlohy" value={jobEvent?.jobName} />
							<ReadOnlyField
								label="ID Publikace"
								value={jobEvent?.publicationId}
							/>
							<ReadOnlyField label="Typ úlohy" value={jobEvent?.krameriusJob} />
							<ReadOnlyField
								label="Parametre"
								value={Object.entries(jobEvent.parameters).map(
									([key, value]) => (
										<ReadOnlyField
											key={key}
											label={key}
											value={JSON.stringify(value)}
										/>
									),
								)}
							/>
						</Box>
						<Box sx={{ p: 4 }}>
							<Button
								className={classes.button}
								color="primary"
								variant="contained"
								onClick={onPublicationButtonClick}
							>
								Zobrazit publikaci
							</Button>
						</Box>
					</Box>
					<Box paddingBottom={2}>
						{jobEvent.executions.length > 0 &&
							jobEvent.executions[jobEvent.executions.length - 1].status ===
								'FAILED' && (
								<Button
									color="primary"
									type="submit"
									variant="contained"
									onClick={handleRestart}
								>
									Restartovat
								</Button>
							)}
					</Box>
				</Grid>
				<Grid item xs>
					<JobExecutionList
						executions={jobEvent.executions}
						onRowClick={handleExecutionClick}
					/>
				</Grid>
				{selectedExecution && (
					<Grid item xs>
						<Paper elevation={3}>
							<JobExecutionDetail jobExecution={selectedExecution} />
						</Paper>
					</Grid>
				)}
			</Grid>
		</Paper>
	)
}
