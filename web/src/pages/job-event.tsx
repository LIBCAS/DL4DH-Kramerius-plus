import { Grid } from '@material-ui/core'
import { ExportKrameriusJob } from 'enums/export-kramerius-job'
import { useState } from 'react'
import { JobEventFilter } from '../modules/jobs/job-event/job-event-filter'
import { JobEventFilterDto } from '../modules/jobs/job-event/job-event-filter-dto'
import { JobEventList } from '../modules/jobs/job-event/job-event-list'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { JobType } from 'enums/job-type'

type Props = {
	jobType: JobType
}

export const JobPage = ({ jobType }: Props) => {
	const [filter, setFilter] = useState<JobEventFilterDto>({})

	const onFilterSubmit = (filter: JobEventFilterDto) => {
		setFilter({ ...filter })
	}

	return (
		<Grid container direction="column" spacing={3}>
			<Grid item xs={12}>
				<JobEventFilter
					jobTypes={
						jobType === JobType.Enriching
							? Object.values(EnrichmentKrameriusJob)
							: Object.values(ExportKrameriusJob)
					}
					onSubmit={onFilterSubmit}
				></JobEventFilter>
			</Grid>
			<Grid item xs={12}>
				<JobEventList filter={filter} jobType={jobType} />
			</Grid>
		</Grid>
	)
}
