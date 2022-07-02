import { Typography } from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { KrameriusJob } from 'enums/kramerius-job'

type Props = {
	krameriusJob: KrameriusJob
}

export const ConfigPrimaryText = ({ krameriusJob }: Props) => {
	return (
		<Typography component="div" variant="h6">
			{KrameriusJobMapping[krameriusJob]}
		</Typography>
	)
}
