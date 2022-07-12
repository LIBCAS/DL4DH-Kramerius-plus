import { EnrichmentJobEventConfig } from '../../../models/job/config/enrichment/enrichment-job-event-config'
import { Fragment } from 'react'
import { Typography } from '@mui/material'
import { MissingAltoStrategyMapping } from '../../../components/mappings/missing-alto-strategy-mapping'
import { ExternalEnrichmentJobEventConfig } from '../../../models/job/config/enrichment/external-enrichment-job-event-config'

type Props = {
	config: EnrichmentJobEventConfig
}

function isExternalConfig(
	config: EnrichmentJobEventConfig,
): config is ExternalEnrichmentJobEventConfig {
	return 'missingAltoOption' in config
}

export const ConfigSecondaryText = ({ config }: Props) => {
	return (
		<Fragment>
			<Typography color="text.secondary" variant="body2">
				Přepsat: {config.override ? 'Ano' : 'Ne'}
			</Typography>
			{isExternalConfig(config) && (
				<Typography color="text.secondary" variant="body2">
					Stratégia při chybějícím ALTO:{' '}
					{MissingAltoStrategyMapping[config.missingAltoOption]}
				</Typography>
			)}
		</Fragment>
	)
}
