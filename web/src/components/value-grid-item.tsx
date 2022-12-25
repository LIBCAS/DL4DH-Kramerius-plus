import { Grid, Typography } from '@mui/material'
import { FC } from 'react'

export const ValueGridItem: FC<{ xs?: number }> = ({ children, xs }) => (
	<Grid display="flex" item justifyContent="flex-end" xs={xs ? xs : 9}>
		<Typography color="primary" fontSize={13} variant="body1">
			{children}
		</Typography>
	</Grid>
)
