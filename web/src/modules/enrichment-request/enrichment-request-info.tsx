import { Divider, Grid, Typography } from '@mui/material'
import { JobEventDataRow } from 'components/job-event/job-event-data-row'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { FC } from 'react'
import { formatDateTime } from 'utils/formatters'

type Props = {
	request: EnrichmentRequest
}

export const EnrichmentRequestInfo: FC<Props> = ({ request }) => {
	return (
		<Grid container spacing={2} sx={{ p: 2 }}>
			<Grid item xs={12}>
				<Typography variant="h5">Detail žádosti o obohacení</Typography>
			</Grid>
			<Grid item xs={12}>
				<Divider />
			</Grid>
			<Grid container item spacing={2}>
				<JobEventDataRow label="Vytvořil" value={request.owner.username} />
				<JobEventDataRow
					label="Vytvořeno v"
					value={formatDateTime(request.created)}
				/>
				<JobEventDataRow label="Název" value={request.name ?? '-'} />
			</Grid>
		</Grid>
	)
}
