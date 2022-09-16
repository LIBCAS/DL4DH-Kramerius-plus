import { Grid } from '@mui/material'
import { ExportRequestFilter } from 'modules/export-request/export-request-filter'
import { ExportRequestList } from 'modules/export-request/export-request-list'
import { PageWrapper } from 'pages/page-wrapper'
import { FC } from 'react'

export const ExportRequestListPage: FC = () => {
	return (
		<PageWrapper>
			<Grid container direction="column" spacing={3}>
				<Grid item xs={12}>
					<ExportRequestFilter />
				</Grid>
				<Grid item xs={12}>
					<ExportRequestList />
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
