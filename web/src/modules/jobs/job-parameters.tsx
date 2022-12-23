import { Box, Grid, Typography } from '@mui/material'
import { MapType } from 'models/map-type'
import { FC } from 'react'

export const JobParameters: FC<{ jobParameters: MapType }> = ({
	jobParameters,
}) => {
	const valueElement = (value: string | Date | number) => (
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
		<Box p={1}>
			<Box pb={3}>
				<Typography variant="h6">Parametre úlohy</Typography>
			</Box>
			<Grid container justifyContent="space-between" spacing={1}>
				{Object.keys(jobParameters).map((key, i) => (
					<Grid key={i} container item xs={12}>
						{keyElement(key)}
						{valueElement(jobParameters[key])}
					</Grid>
				))}
			</Grid>
		</Box>
	)
}
