import { Grid, Button } from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'

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
					lg={6}
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
