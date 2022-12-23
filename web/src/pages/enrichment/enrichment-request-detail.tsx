import { Divider, Grid, Paper, Typography } from '@mui/material'
import { getEnrichmentRequest } from 'api/enrichment-api'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { EnrichmentRequestInfo } from 'modules/enrichment-request/enrichment-request-info'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { PageWrapper } from '../page-wrapper'
import { Loading } from 'components/loading'
import { EnrichmentRequestItemGrid } from 'modules/enrichment-request/enrichment-request-item-grid'
import { KrameriusJobInstanceSummary } from 'modules/kramerius-job-instance/kramerius-job-instance-summary'

export const EnrichmentRequestDetail: FC = () => {
	const [enrichmentRequest, setEnrichmentRequest] =
		useState<EnrichmentRequest>()
	const { requestId } = useParams()

	useEffect(() => {
		async function fetchEnrichmentRequest() {
			if (requestId) {
				const enrichmentRequest = await getEnrichmentRequest(requestId)
				setEnrichmentRequest(enrichmentRequest)
			}
		}

		fetchEnrichmentRequest()
	}, [requestId])

	return (
		<PageWrapper requireAuth>
			{enrichmentRequest ? (
				<Paper sx={{ p: 2 }}>
					<Grid container spacing={2} sx={{ p: 2 }}>
						<Grid item xs={12}>
							<Typography variant="h5">Detail žádosti o obohacení</Typography>
						</Grid>
						<Grid item xs={12}>
							<Divider />
						</Grid>
						<Grid item xs={12}>
							<EnrichmentRequestInfo request={enrichmentRequest} />
						</Grid>
						<Grid item xs={12}>
							<Divider />
						</Grid>
						<Grid item xs={12}>
							<EnrichmentRequestItemGrid items={enrichmentRequest.items} />
							{/* <EnrichmentRequestPlans plans={enrichmentRequest.jobPlans} /> */}
						</Grid>
					</Grid>
				</Paper>
			) : (
				<Loading />
			)}
		</PageWrapper>
	)
}
