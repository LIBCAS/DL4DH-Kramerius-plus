import { Grid } from '@mui/material'
import { EnrichmentRequestFilter } from 'modules/enrichment-request/enrichment-request-filter'
import { EnrichmentRequestList } from 'modules/enrichment-request/enrichment-request-list'
import { FC } from 'react'
import { PageWrapper } from './page-wrapper'

export const EnrichmentRequestListPage: FC = () => {
	return (
		// <PageWrapper requireAuth >
		<PageWrapper>
			<Grid container direction="column" spacing={3}>
				<Grid item xs={12}>
					<EnrichmentRequestFilter />
				</Grid>
				<Grid item xs={12}>
					<EnrichmentRequestList />
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
