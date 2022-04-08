import { Grid } from '@material-ui/core'
import { GridRowParams } from '@mui/x-data-grid'
import { JobEvent } from 'models/job-event'
import { QueryResult } from 'models/query-results'
import { useEffect, useState } from 'react'
import { getJobEvent, listJobEvents } from './job-api'
import { JobEventDetail } from './job-event/job-event-detail'
import { JobEventList } from './job-event/job-event-list'

type Props = {
	jobName: string
}

export const JobPage = ({ jobName }: Props) => {
	const [jobEvents, setJobEvents] = useState<QueryResult<JobEvent>>()
	const [selectedJob, setSelectedJob] = useState<JobEvent>()

	useEffect(() => {
		async function fetchInstances() {
			const response = await listJobEvents(jobName)
			setJobEvents(response)
		}
		fetchInstances()

		return () => {
			setSelectedJob(undefined)
			setJobEvents(undefined)
		}
	}, [jobName])

	const onRowClickCallback = (params: GridRowParams) => {
		async function fetchJobDetail() {
			const jobEvent = await getJobEvent(params.row['id'])
			console.log(jobEvent)
			setSelectedJob(jobEvent)
		}
		fetchJobDetail()
	}

	return (
		<Grid container spacing={2}>
			{jobEvents && (
				<Grid item xs={4}>
					<JobEventList
						jobs={jobEvents?.results}
						onRowClick={onRowClickCallback}
					/>
				</Grid>
			)}
			{selectedJob && (
				<Grid item xs={8}>
					<JobEventDetail jobEvent={selectedJob} />
				</Grid>
			)}
		</Grid>
	)
}
