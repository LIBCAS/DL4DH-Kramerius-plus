import { Grid } from '@mui/material'
import { ExportKrameriusJob } from '../enums/export-kramerius-job'
import { FC, useState } from 'react'
import { JobEventFilter } from '../modules/jobs/job-event/job-event-filter'
import { JobEventFilterDto } from '../modules/jobs/job-event/job-event-filter-dto'
import { JobEventList } from '../components/job-event/job-event-list'
import { EnrichmentKrameriusJob } from '../enums/enrichment-kramerius-job'
import { useParams } from 'react-router-dom'
import { Loading } from 'components/loading'
import { JobType } from 'enums/job-type'
import { PageWrapper } from './page-wrapper'

export const JobEventListPage: FC = () => {
	const [filter, setFilter] = useState<JobEventFilterDto>({})
	const { jobType } = useParams()

	const onFilterSubmit = (filter: JobEventFilterDto) => {
		setFilter({ ...filter })
	}

	return jobType ? (
		<PageWrapper requireAuth>
			<Grid container direction="column" spacing={3}>
				<Grid item xs={12}>
					<JobEventFilter
						jobTypes={
							jobType === 'enriching'
								? Object.values(EnrichmentKrameriusJob)
								: Object.values(ExportKrameriusJob)
						}
						onSubmit={onFilterSubmit}
					/>
				</Grid>
				<Grid item xs={12}>
					<JobEventList filter={filter} jobType={jobType as JobType} />
				</Grid>
			</Grid>
		</PageWrapper>
	) : (
		<Loading />
	)
}
