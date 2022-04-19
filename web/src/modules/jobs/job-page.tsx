import { Grid } from '@material-ui/core'
import { GridRowParams } from '@mui/x-data-grid'
import { JobEvent } from 'models/job-event'
import { JobType } from 'models/job-type'
import { useEffect, useState } from 'react'
import { useHistory, useParams } from 'react-router'
import { getJobEvent, listJobEvents } from './job-api'
import { JobEventDetail } from './job-event/job-event-detail'
import { JobEventList } from './job-event/job-event-list'

type Props = {
	jobType: JobType
}

export const JobPage = ({ jobType }: Props) => {
	const [jobEvents, setJobEvents] = useState<JobEvent[]>()
	const [selectedJob, setSelectedJob] = useState<JobEvent>()
	const { jobId } = useParams<{ jobId: string }>()
	const { replace } = useHistory()

	async function fetchJobDetail(jobId: string) {
		const jobEvent = await getJobEvent(jobId)
		setSelectedJob(jobEvent)
	}

	useEffect(() => {
		async function fetchInstances() {
			const response = await listJobEvents(jobType)
			setJobEvents(response)
		}
		fetchInstances()

		if (jobId) {
			fetchJobDetail(jobId)
		}

		return () => {
			setSelectedJob(undefined)
			setJobEvents(undefined)
		}
	}, [jobType])

	const onRowClickCallback = (params: GridRowParams) => {
		fetchJobDetail(params.row['id'])
		replace(`/jobs/enriching/${params.row['id']}`)
	}

	return (
		<Grid container spacing={2}>
			{jobEvents && (
				<Grid item xs={4}>
					<JobEventList jobs={jobEvents} onRowClick={onRowClickCallback} />
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
