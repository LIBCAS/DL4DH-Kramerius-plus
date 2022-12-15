import { Button, Divider, Grid, Typography } from '@mui/material'
import { download } from 'api/file-ref-api'
import { JobEventDataRow } from 'components/job-event/job-event-data-row'
import { ExportRequest } from 'models/request/export-request'
import { FC } from 'react'
import { formatDateTime } from 'utils/formatters'

type Props = {
	request: ExportRequest
}

export const ExportRequestDetail: FC<Props> = ({ request }) => {
	return (
		<Grid container spacing={2} sx={{ p: 2 }}>
			{/* <Grid container direction="row" item spacing={2}>
				<Grid item xs={10}>
					<Typography variant="h5">Detail žádosti o export</Typography>
				</Grid>
				<Grid item xs={2}>
					<Button
						disabled={!request.bulkExport?.fileRef}
						fullWidth
						variant="contained"
						onClick={download(request.bulkExport?.fileRef?.id ?? '')}
					>
						Stáhnout hromadný export
					</Button>
				</Grid>
			</Grid>
			<Grid item xs={12}>
				<Divider />
			</Grid>
			<JobEventDataRow label="Vytvořil" value={request.owner.username} />
			<JobEventDataRow
				label="Vytvořeno v"
				value={formatDateTime(request.created)}
			/>
			<JobEventDataRow
				label="Formát"
				value={request.bulkExport?.format ?? '-'}
			/>
			<JobEventDataRow label="Název" value={request.name ?? '-'} /> */}
		</Grid>
	)
}
