import { Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { MapType } from 'models/map-type'
import { FC } from 'react'

export const JobParameters: FC<{ jobParameters: MapType }> = ({
	jobParameters,
}) => {
	return (
		<PageBlock title="Parametre úlohy">
			{Object.keys(jobParameters).map((key, i) => (
				<Grid key={i} container item xs={12}>
					<KeyGridItem>{key}</KeyGridItem>
					<ValueGridItem>{jobParameters[key]}</ValueGridItem>
				</Grid>
			))}
		</PageBlock>
	)
}
