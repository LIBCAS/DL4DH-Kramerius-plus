import { Grid, Typography } from '@mui/material'
import { FC } from 'react'

type Props = {
	label: string
	value: string
	valueComponent?: React.ElementType
}

export const JobEventDataRow: FC<Props> = ({
	label,
	value,
	valueComponent,
}) => {
	return (
		<>
			<Grid item lg={2} md={4} xs={4}>
				<Typography color="text.primary" variant="body2">
					{label}
				</Typography>
			</Grid>
			<Grid item lg={10} md={8} xs={8}>
				<Typography
					color="primary"
					component={valueComponent || 'div'}
					variant="body2"
				>
					{value}
				</Typography>
			</Grid>
		</>
	)
}
