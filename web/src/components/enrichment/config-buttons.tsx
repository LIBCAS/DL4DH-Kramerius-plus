import { Button, Grid } from '@mui/material'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { KrameriusJobMapping } from '../mappings/kramerius-job-mapping'

type Props = {
	onClick: (krameriusJob: EnrichmentKrameriusJob) => void
}

export const ConfigButtons = ({ onClick }: Props) => {
	const handleOnClick = (krameriusJob: EnrichmentKrameriusJob) => () => {
		onClick(krameriusJob)
	}

	return (
		<Grid container spacing={2}>
			{Object.values(EnrichmentKrameriusJob).map((krameriusJob, i) => (
				<Grid
					key={i}
					item
					justifyContent="center"
					lg={4}
					textAlign="center"
					xs={12}
				>
					<Button
						color="primary"
						fullWidth
						variant="contained"
						onClick={handleOnClick(krameriusJob)}
					>
						{KrameriusJobMapping[krameriusJob]}
					</Button>
				</Grid>
			))}
		</Grid>
	)
}
