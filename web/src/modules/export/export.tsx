import Grid from '@material-ui/core/Grid'

import { PublicationsForExport } from './publications-for-export'
import { ExportedPublications } from './exported-publications'

export const Export = () => {
	return (
		<Grid container spacing={2}>
			<Grid item xs={6}>
				<PublicationsForExport />
			</Grid>
			<Grid item xs={6}>
				<ExportedPublications />
			</Grid>
		</Grid>
	)
}
