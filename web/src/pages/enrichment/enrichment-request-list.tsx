import { Grid } from '@mui/material'
import { EnrichmentRequestFilter } from 'modules/enrichment-request/enrichment-request-filter'
import { EnrichmentRequestGrid } from 'modules/enrichment-request/enrichment-request-grid'
import { FC, useState } from 'react'
import { PageWrapper } from '../page-wrapper'

export type EnrichmentRequestFilterDto = {
	name?: string
	owner?: string
	isFinished?: boolean
	publicationId?: string
}

export const EnrichmentRequestList: FC = () => {
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
					<EnrichmentRequestGrid filter={filter} />
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
