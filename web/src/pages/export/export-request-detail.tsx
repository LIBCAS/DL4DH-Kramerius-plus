import { Box, Grid, Paper, Typography } from '@mui/material'
import { getExportRequest } from 'api/export-api'
import { Loading } from 'components/loading'
import { ExportRequest } from 'models/request/export-request'
import { ExportRequestConfig } from 'modules/export-request/export-request-config'
import { ExportRequestInfo } from 'modules/export-request/export-request-info'
import { ExportRequestItems } from 'modules/export-request/export-request-items'
import { ExportRequestResult } from 'modules/export-request/export-request-result'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

export const ExportRequestDetailPage: FC = () => {
	const [exportRequest, setExportRequest] = useState<ExportRequest>()
	const { requestId } = useParams()

	useEffect(() => {
		async function fetchExportRequest() {
			if (requestId) {
				const exportRequest = await getExportRequest(requestId)
				console.log(exportRequest)
				setExportRequest(exportRequest)
			}
		}

		fetchExportRequest()
	}, [requestId])

	return (
		<PageWrapper requireAuth>
			{exportRequest ? (
				<Box>
					<Box display="flex" justifyContent="space-between" p={1} pb={3}>
						<Typography variant="h4">Detail žádosti o export</Typography>
					</Box>
					<Grid container spacing={1}>
						<Grid item sx={{ p: 2 }} xs={4}>
							<Paper elevation={4} sx={{ height: 200, p: 2 }}>
								<ExportRequestInfo request={exportRequest} />
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={4}>
							<Paper elevation={4} sx={{ height: 200, p: 2 }}>
								<ExportRequestConfig config={exportRequest.config} />
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={4}>
							<Paper elevation={4} sx={{ height: 200, p: 2 }}>
								<ExportRequestResult bulkExport={exportRequest.bulkExport} />
							</Paper>
						</Grid>
						<Grid item sx={{ p: 2 }} xs={12}>
							<Paper elevation={4} sx={{ height: 400, p: 2 }}>
								<ExportRequestItems
									items={exportRequest.items.map(item =>
										item.rootExport ? item.rootExport : item,
									)}
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
