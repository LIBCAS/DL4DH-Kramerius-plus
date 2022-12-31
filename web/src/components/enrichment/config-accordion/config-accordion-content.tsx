import { Grid } from '@mui/material'
import { EnrichmentKrameriusJob } from '../../../enums/enrichment-kramerius-job'
import { EnrichmentJobConfig } from '../../../models/job/config/enrichment-job-config'
import { ConfigButtons } from './config-buttons'
import { ConfigList } from './config-list'

type Props = {
	configs: EnrichmentJobConfig[]
	onConfigClick: (index: number) => void
	onConfigRemove: (index: number) => void
	onNewConfigClick: (KrameriusJob: EnrichmentKrameriusJob) => void
}

export const ConfigAccordionContent = ({
	configs,
	onConfigClick,
	onConfigRemove,
	onNewConfigClick,
}: Props) => {
	return (
		<Grid container spacing={2}>
			<Grid item xs={12}>
				<ConfigList
					configs={configs}
					onClick={onConfigClick}
					onRemove={onConfigRemove}
				/>
			</Grid>
			<Grid item xs={12}>
				<ConfigButtons onClick={onNewConfigClick} />
			</Grid>
		</Grid>
	)
}
