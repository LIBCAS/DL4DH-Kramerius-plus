import { Grid } from '@material-ui/core'
import { GridRowParams } from '@mui/x-data-grid'
import { JobInstance } from 'models/job-instance'
import { useEffect, useState } from 'react'
import { getJobInstances } from './job-api'
import { JobDetail } from './job-detail/job-detail'
import { JobList } from './job-list'

type Props = {
	jobName: string
}

export const JobPage = ({ jobName }: Props) => {
	const [jobInstances, setJobInstances] = useState<JobInstance[]>([])
	const [selectedJob, setSelectedJob] = useState<JobInstance>()

	useEffect(() => {
		async function fetchInstances() {
			const response = await getJobInstances(jobName)
			setJobInstances(response)
		}
		fetchInstances()
	}, [jobName])

	const onRowClickCallback = (params: GridRowParams) => {
		const job: JobInstance = {
			id: params.row['id'],
			jobName: params.row['jobName'],
		}
		setSelectedJob(job)
	}

	return (
		<Grid container spacing={2}>
			<Grid item xs={4}>
				<JobList jobs={jobInstances} onRowClick={onRowClickCallback} />
			</Grid>
			<Grid item xs={8}>
				<JobDetail job={selectedJob} />
			</Grid>
		</Grid>
	)
}
