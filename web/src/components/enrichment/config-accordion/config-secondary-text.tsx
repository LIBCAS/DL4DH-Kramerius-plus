import { Typography } from '@mui/material'
import { Fragment } from 'react'
import { EnrichmentJobEventConfig } from '../../../models/job/config/enrichment-job-event-config'

type Props = {
	config: EnrichmentJobEventConfig
}

export const ConfigSecondaryText = ({ config }: Props) => {
	return (
		<Fragment>
			<Typography color="text.secondary" variant="body2">
				Přepsat: {config.override ? 'Ano' : 'Ne'}
			</Typography>
		</Fragment>
	)
}
