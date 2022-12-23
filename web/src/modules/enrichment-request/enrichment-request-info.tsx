import { Button, Divider, Grid, Typography } from '@mui/material'
import { DataRow } from 'components/job-event/data-row'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { FC } from 'react'
import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import { formatDateTime } from 'utils/formatters'

type Props = {
	request: EnrichmentRequest
}

export const EnrichmentRequestInfo: FC<Props> = ({ request }) => {
	const navigate = useNavigate()

	return (
		<Grid container spacing={2} sx={{ p: 1 }}>
			<Grid alignContent="flex-start" container item lg={4} spacing={2} xs={12}>
				<DataRow label="Vytvořil" value={request.owner.username} />
				<DataRow label="Vytvořeno v" value={formatDateTime(request.created)} />
				<DataRow label="Název" value={request.name ?? '-'} />
				<Grid container item justifyContent="space-between" xs={12}>
					<Grid item xs={6}>
						<Typography color="text.primary" variant="body2">
							Stav inicializační úlohy
						</Typography>
					</Grid>
					<Grid item xs={6}>
						<Link to={`/jobs/${request.createRequestJob.id}`}>
							{request.createRequestJob.executionStatus}
						</Link>
					</Grid>
				</Grid>
			</Grid>
			<Grid container item justifyContent="space-between" lg={8} xs={12}>
				<Grid container item xs={12}>
					<Typography color="text.primary" variant="body1">
						Konfigurace:
					</Typography>
				</Grid>
				<Grid container item xs={12}>
					{request.configs.map((config, i) => (
						<Grid key={config.id} container xs={12}>
							<Grid item xs={1}>
								<Typography color="primary" variant="body1">
									{`${i + 1}.`}
								</Typography>
							</Grid>
							<Grid item xs={11}>
								<Typography color="primary" variant="body1">
									{KrameriusJobMapping[config.jobType]}
								</Typography>
							</Grid>
							<Grid item xs={1}></Grid>
							<Grid container item xs={8}>
								<DataRow
									label="Přepsat existující"
									value={config.override ? 'ANO' : 'NE'}
								/>
								<DataRow
									label="Tolerance chyb"
									value={config.pageErrorTolerance.toString()}
								/>
							</Grid>
						</Grid>
					))}
				</Grid>
			</Grid>
		</Grid>
	)
}
