import { Grid } from '@mui/material'
import { ExportRequestFilter } from 'modules/export-request/export-request-filter'
import { ExportRequestGrid } from 'modules/export-request/export-request-grid'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useState } from 'react'

export interface ExportRequestFilterDto {
	name?: string
	owner?: string
	isFinished?: boolean
}

export const ExportRequestListPage: FC = () => {
	const [filter, setFilter] = useState<ExportRequestFilterDto>()

	const onFilterSubmit = (filter: ExportRequestFilterDto) => {
		setFilter({ ...filter })
	}

	return (
		<PageWrapper requireAuth>
			<Grid container direction="column" spacing={3}>
				<Grid item xs={12}>
					<ExportRequestFilter onSubmit={onFilterSubmit} />
				</Grid>
				<Grid item xs={12}>
					<ExportRequestGrid filter={filter} />
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
