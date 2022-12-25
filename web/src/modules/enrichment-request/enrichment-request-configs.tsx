import { Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { EnrichmentJobEventConfig } from 'models/job/config/enrichment-job-event-config'
import { FC } from 'react'

export const EnrichmentRequestConfigs: FC<{
	configs: EnrichmentJobEventConfig[]
}> = ({ configs }) => {
	return (
		<PageBlock title="Konfigurace úloh žádosti">
			<Grid container spacing={4}>
				{configs.map((config, i) => (
					<Grid key={i} container item xs={4}>
						<KeyGridItem fontSize={18} variant="h6" xs={12}>
							{i + 1}. Úloha
						</KeyGridItem>
						<KeyGridItem>Typ úlohy</KeyGridItem>
						<ValueGridItem>{KrameriusJobMapping[config.jobType]}</ValueGridItem>
						<KeyGridItem>Přepsat</KeyGridItem>
						<ValueGridItem>{config.override ? 'ANO' : 'NE'}</ValueGridItem>
						<KeyGridItem xs={4}>Chybová tolerance</KeyGridItem>
						<ValueGridItem xs={8}>{config.pageErrorTolerance}</ValueGridItem>
					</Grid>
				))}
			</Grid>
		</PageBlock>
	)
}
