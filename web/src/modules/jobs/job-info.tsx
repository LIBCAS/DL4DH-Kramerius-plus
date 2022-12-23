import { Grid, Typography } from '@mui/material'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { KrameriusJobInstance } from 'models/job/kramerius-job-instance'
import { FC } from 'react'

export const JobInfo: FC<{ job: KrameriusJobInstance }> = ({ job }) => {
	const valueElement = (value: string) => (
		<Grid display="flex" item justifyContent="flex-end" xs={9}>
			<Typography color="primary" fontSize={13} variant="body1">
				{value}
			</Typography>
		</Grid>
	)

	const keyElement = (key: string) => (
		<Grid item xs={3}>
			<Typography fontSize={13} variant="subtitle1">
				{key}
			</Typography>
		</Grid>
	)

	return (
		<Grid container spacing={1} sx={{ p: 1 }}>
			<Grid item sx={{ pb: 3 }} xs={12}>
				<Typography variant="h6">Základní informace</Typography>
			</Grid>
			<Grid container item spacing={1} xs={12}>
				{keyElement('ID')}
				{valueElement(job.id)}
				{keyElement('Typ úlohy')}
				{valueElement(KrameriusJobMapping[job.jobType])}
				{keyElement('Stav úlohy')}
				{valueElement(job.executionStatus)}
			</Grid>
		</Grid>
	)
}
