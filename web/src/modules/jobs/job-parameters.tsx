import { Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { MapType } from 'models/map-type'
import { FC, Fragment } from 'react'

export const JobParameters: FC<{ jobParameters: MapType }> = ({
	jobParameters,
}) => {
	return (
		<PageBlock title="Parametre úlohy">
			<Grid container spacing={1}>
				{Object.keys(jobParameters).map((key, i) => (
					<Fragment key={i}>
						<KeyGridItem>{key}</KeyGridItem>
						<ValueGridItem>{jobParameters[key]}</ValueGridItem>
					</Fragment>
				))}
			</Grid>
		</PageBlock>
	)
}
