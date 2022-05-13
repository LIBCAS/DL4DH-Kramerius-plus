import { Grid } from '@material-ui/core'
import { GridRowParams } from '@mui/x-data-grid'
import { JobType } from 'models/job-type'
import { useEffect, useState } from 'react'
import { useHistory, useParams } from 'react-router'
import { JobEventDetail } from './job-event/job-event-detail'
import { JobEventList } from './job-event/job-event-list'

type Props = {
	jobType: JobType
}

export const JobPage = ({ jobType }: Props) => {
	const [selectedJob, setSelectedJob] = useState<string>()
	const { jobId } = useParams<{ jobId: string }>()
	const { replace } = useHistory()

	useEffect(() => {
		if (jobId) {
			setSelectedJob(jobId)
		}
	}, [jobId, jobType])

	const onRowClickCallback = (params: GridRowParams) => {
		replace(`/jobs/${JobType[jobType].toLocaleLowerCase()}/${params.row['id']}`)
		setSelectedJob(params.row['id'])
	}

	return (
		<Grid container spacing={2}>
			<Grid item xs={4}>
				<JobEventList jobType={jobType} onRowClick={onRowClickCallback} />
			</Grid>
			{selectedJob && (
				<Grid item xs={8}>
					<JobEventDetail jobEventId={selectedJob} />
				</Grid>
			)}
		</Grid>
	)
}
