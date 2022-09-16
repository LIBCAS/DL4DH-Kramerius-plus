import { Grid } from '@mui/material'
import { EnrichmentRequestFilterDto } from 'models/enrichment-request/enrichment-request-filter-dto'
import { EnrichmentRequestFilter } from 'modules/enrichment-request/enrichment-request-filter'
import { EnrichmentRequestList } from 'modules/enrichment-request/enrichment-request-list'
import { FC, useState } from 'react'
import { PageWrapper } from '../page-wrapper'

export const EnrichmentRequestListPage: FC = () => {
	const [filter, setFilter] = useState<EnrichmentRequestFilterDto>({})

	const onFilterSubmit = (filter: EnrichmentRequestFilterDto) => {
		setFilter({ ...filter })
	}

	return (
		<PageWrapper requireAuth>
			<Grid container direction="column" spacing={3}>
				<Grid item xs={12}>
					<EnrichmentRequestFilter onSubmit={onFilterSubmit} />
				</Grid>
				<Grid item xs={12}>
					<EnrichmentRequestList filter={filter} />
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
