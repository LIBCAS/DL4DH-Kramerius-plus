import { Typography } from '@mui/material'
import { Fragment } from 'react'
import { EnrichmentJobConfig } from '../../../models/job/config/enrichment-job-config'

type Props = {
	config: EnrichmentJobConfig
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
