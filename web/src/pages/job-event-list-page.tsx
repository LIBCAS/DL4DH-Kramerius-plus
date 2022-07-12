import { Grid } from '@mui/material'
import { ExportKrameriusJob } from '../enums/export-kramerius-job'
import { FC, useState } from 'react'
import { JobEventFilter } from '../modules/jobs/job-event/job-event-filter'
import { JobEventFilterDto } from '../modules/jobs/job-event/job-event-filter-dto'
import { JobEventList } from '../components/job-event/job-event-list'
import { EnrichmentKrameriusJob } from '../enums/enrichment-kramerius-job'
import { JobType } from '../enums/job-type'
import { RouteComponentProps } from 'react-router'

export const JobEventListPage: FC<
	RouteComponentProps<{ jobType: JobType }>
> = ({ match }) => {
	const [filter, setFilter] = useState<JobEventFilterDto>({})

	const onFilterSubmit = (filter: JobEventFilterDto) => {
		setFilter({ ...filter })
	}

	return (
		<Grid container direction="column" spacing={3}>
			<Grid item xs={12}>
				<JobEventFilter
					jobTypes={
						match.params.jobType === 'enriching'
							? Object.values(EnrichmentKrameriusJob)
							: Object.values(ExportKrameriusJob)
					}
					onSubmit={onFilterSubmit}
				></JobEventFilter>
			</Grid>
			<Grid item xs={12}>
				<JobEventList filter={filter} jobType={match.params.jobType} />
			</Grid>
		</Grid>
	)
}
