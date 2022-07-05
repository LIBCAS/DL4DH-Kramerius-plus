import { Paper, Grid } from '@mui/material'
import { EnrichmentForm } from '../components/enrichment/enrichment-form'

export const EnrichmentPage = () => {
	return (
		<Grid container justifyContent="center">
			<Grid item lg={7} md={8} sm={10} xl={6} xs={12}>
				<Paper>
					<EnrichmentForm />
				</Paper>
			</Grid>
		</Grid>
	)
}
