import { Paper, Grid } from '@mui/material'
import { FC } from 'react'
import { EnrichmentForm } from '../../components/enrichment/enrichment-form'
import { PageWrapper } from '../page-wrapper'

export const EnrichmentPage: FC = () => {
	return (
		<PageWrapper requireAuth>
			<Grid container justifyContent="center">
				<Grid item lg={7} md={8} sm={10} xl={6} xs={12}>
					<Paper>
						<EnrichmentForm />
					</Paper>
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
