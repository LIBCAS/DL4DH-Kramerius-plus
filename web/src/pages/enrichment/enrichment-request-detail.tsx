import { Divider, Grid, Paper } from '@mui/material'
import { getEnrichmentRequest } from 'api/enrichment-api'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { EnrichmentRequestInfo } from 'modules/enrichment-request/enrichment-request-info'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { PageWrapper } from '../page-wrapper'
import { Loading } from 'components/loading'

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
					<Grid container>
						<Grid item xs={12}>
							<EnrichmentRequestInfo request={enrichmentRequest} />
						</Grid>
						<Grid item xs={12}>
							<Divider sx={{ m: 2 }} />
						</Grid>
						<Grid item xs={12}>
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
