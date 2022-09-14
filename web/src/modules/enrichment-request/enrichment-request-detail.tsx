import { Grid } from '@mui/material'
import { JobEventDataRow } from 'components/job-event/job-event-data-row'
import { EnrichmentRequest } from 'models/enrichment-request'
import { FC } from 'react'
import { formatDateTime } from 'utils/formatters'

type Props = {
	request: EnrichmentRequest
}

export const EnrichmentRequestDetail: FC<Props> = ({ request }) => {
	return (
		<Grid container spacing={2} sx={{ m: 2 }}>
			<JobEventDataRow label="Vytvořil" value={request.owner.username} />
			<JobEventDataRow
				label="Vytvořeno v"
				value={formatDateTime(request.created)}
			/>
			<JobEventDataRow label="Název" value={request.name ?? '-'} />
		</Grid>
	)
}
