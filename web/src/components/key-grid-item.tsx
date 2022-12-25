import { Grid, Typography } from '@mui/material'
import { Variant } from '@mui/material/styles/createTypography'
import { FC } from 'react'

export const KeyGridItem: FC<{
	xs?: number
	fontSize?: number
	variant?: Variant
}> = ({ children, xs, fontSize, variant }) => (
	<Grid item xs={xs ? xs : 3}>
		<Typography
			fontSize={fontSize ? fontSize : 13}
			variant={variant ? variant : 'subtitle1'}
		>
			{children}
		</Typography>
	</Grid>
)
