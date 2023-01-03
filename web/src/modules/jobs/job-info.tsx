import { Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { FC } from 'react'

export const JobInfo: FC<{ job: KrameriusJobInstance }> = ({ job }) => {
	return (
		<PageBlock title="Základní informace">
			<Grid container spacing={1}>
				<KeyGridItem>ID</KeyGridItem>
				<ValueGridItem>{job.id}</ValueGridItem>
				<KeyGridItem>Typ úlohy</KeyGridItem>
				<ValueGridItem>{KrameriusJobMapping[job.jobType]}</ValueGridItem>
				<KeyGridItem>Stav úlohy</KeyGridItem>
				<ValueGridItem>{job.executionStatus}</ValueGridItem>
			</Grid>
		</PageBlock>
	)
}
