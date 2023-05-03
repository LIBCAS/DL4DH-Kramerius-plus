import { Box, Button, Grid, Paper, Typography } from '@mui/material'
import { cancelRequest, getEnrichmentRequest } from 'api/enrichment-api'
import { Loading } from 'components/loading'
import { EnrichmentRequest } from 'models/request/enrichment-request'
import { EnrichmentRequestItem } from 'models/request/enrichment-request-item'
import { EnrichmentChains } from 'modules/enrichment-request/enrichment-chains'
import { EnrichmentRequestConfigs } from 'modules/enrichment-request/enrichment-request-configs'
import { EnrichmentRequestInfo } from 'modules/enrichment-request/enrichment-request-info'
import { EnrichmentRequestItems } from 'modules/enrichment-request/enrichment-request-items'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { toast } from 'react-toastify'
import { PageWrapper } from '../page-wrapper'

export const EnrichmentRequestDetail: FC = () => {
	const [enrichmentRequest, setEnrichmentRequest] =
		useState<EnrichmentRequest>()
	const [selectedItem, setSelectedItem] = useState<EnrichmentRequestItem>()
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

	const onItemClick = (itemId: string) => {
		setSelectedItem(enrichmentRequest?.items.find(item => item.id === itemId))
	}

	const onCancelClick = () => {
		const callApi = async () => {
			const response = await cancelRequest(requestId!)

			console.log(response)

			if (response.ok) {
				toast('Operace proběhla úspěšně', {
					type: 'success',
				})
			} else {
				toast(
					`Při pokusu o zrušení zádosti došlo k chybě: ${response.statusText}`,
					{
						type: 'error',
					},
				)
			}
		}

		callApi()
	}

	return (
		<PageWrapper requireAuth>
			{enrichmentRequest ? (
				<Box>
					<Box
						display="flex"
						justifyContent="space-between"
						p={1}
						pb={3}
						pr={3}
					>
						<Typography variant="h4">Detail žádosti o obohacení</Typography>
						<Button
							disabled={enrichmentRequest.state != 'RUNNING'}
							variant="contained"
							onClick={onCancelClick}
						>
							Zrušit žádost
						</Button>
					</Box>
					<Grid container spacing={1}>
						<Grid item sx={{ p: 2 }} xs={4.3}>
							<Paper elevation={4} sx={{ height: 230, p: 2 }}>
								<EnrichmentRequestInfo request={enrichmentRequest} />
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={7.7}>
							<Paper elevation={4} sx={{ height: 230, p: 2 }}>
								<EnrichmentRequestConfigs configs={enrichmentRequest.configs} />
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={6}>
							<Paper elevation={4} sx={{ height: 400, p: 2 }}>
								<EnrichmentRequestItems
									items={enrichmentRequest.items}
									onItemClick={onItemClick}
								/>
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={6}>
							<Paper elevation={4} sx={{ height: 400, p: 2 }}>
								<EnrichmentChains
									chains={selectedItem ? selectedItem.enrichmentChains : []}
								/>
							</Paper>
						</Grid>
					</Grid>
				</Box>
			) : (
				<Loading />
			)}
		</PageWrapper>
	)
}
