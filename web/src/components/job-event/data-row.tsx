import { Grid, Typography } from '@mui/material'
import { FC } from 'react'

type Props = {
	label: string
	value: string
	valueComponent?: React.ElementType
}

export const DataRow: FC<Props> = ({ label, value, valueComponent }) => {
	return (
		<Grid container item justifyContent="space-between" xs={12}>
			<Grid item xs={6}>
				<Typography color="text.primary" variant="body2">
					{label}
				</Typography>
			</Grid>
			<Grid item xs={6}>
				<Typography
					color="primary"
					component={valueComponent || 'div'}
					variant="body2"
				>
					{value}
				</Typography>
			</Grid>
		</Grid>
	)
}
