import { Divider, Grid, Paper } from '@mui/material'
import { getExportRequest } from 'api/export-api'
import { Loading } from 'components/loading'
import { ExportRequest } from 'models/request/export-request'
import { ExportRequestDetail } from 'modules/export-request/export-request-detail'
import { ExportRequestExportsList } from 'modules/export-request/export-request-export-list'
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
				setExportRequest(exportRequest)
			}
		}

		fetchExportRequest()
	}, [requestId])

	return (
		<PageWrapper requireAuth>
			{exportRequest ? (
				<Paper sx={{ p: 2 }}>
					<Grid container>
						<Grid item xs={12}>
							<ExportRequestDetail request={exportRequest} />
						</Grid>
						<Grid item xs={12}>
							<Divider sx={{ m: 2 }} />
						</Grid>
						<Grid item xs={12}>
							{/* {exportRequest.bulkExport && (
								// <ExportRequestExportsList
								// 	exports={exportRequest.bulkExport?.exports}
								// />
							)} */}
						</Grid>
					</Grid>
				</Paper>
			) : (
				<Loading />
			)}
		</PageWrapper>
	)
}
