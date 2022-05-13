import Grid from '@material-ui/core/Grid'

import { EnrichmentForm } from './enrichment-form'

export const Enrichment = () => {
	return (
		<Grid container spacing={2}>
			<Grid item xs={8}>
				<EnrichmentForm />
			</Grid>
		</Grid>
	)
}
