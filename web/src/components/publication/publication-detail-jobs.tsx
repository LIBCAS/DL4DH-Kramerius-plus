import {
	Grid,
	ToggleButton,
	ToggleButtonGroup,
	Typography,
	useMediaQuery,
} from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { KrameriusJob } from 'enums/kramerius-job'
import { Publication } from 'models'
import { FC } from 'react'
import { PublicationJobEventList } from './publication-job-event-list'

export const PublicationDetailJobs: FC<{
	publication: Publication
	selectedJobType?: KrameriusJob
	handleJobTypeClick: (_: React.MouseEvent<HTMLElement>, value: any) => void
}> = ({ publication, selectedJobType, handleJobTypeClick }) => {
	const horizontalButtonGroups = useMediaQuery('(min-width:901px)', {
		noSsr: true,
	})

	return (
		<Grid container spacing={2} sx={{ p: 2 }}>
			<Grid item xs={12}>
				<Typography variant="h6">Úlohy</Typography>
			</Grid>
			<Grid item xs={12}>
				<ToggleButtonGroup
					exclusive
					fullWidth
					orientation={`${horizontalButtonGroups ? `horizontal` : `vertical`}`}
					size="small"
					value={selectedJobType}
					onChange={handleJobTypeClick}
				>
					{Object.values(EnrichmentKrameriusJob).map(jobType => (
						<ToggleButton
							key={jobType.toString()}
							color="primary"
							value={jobType as EnrichmentKrameriusJob}
						>
							{KrameriusJobMapping[jobType]}
						</ToggleButton>
					))}
				</ToggleButtonGroup>
			</Grid>
			<Grid item xs={12}>
				{selectedJobType !== undefined && (
					<PublicationJobEventList
						krameriusJob={selectedJobType}
						publicationId={publication.id}
					/>
				)}
			</Grid>
		</Grid>
	)
}
