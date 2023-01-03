import { Box, CircularProgress } from '@mui/material'
import { FC } from 'react'

export const Loading: FC = () => {
	return (
		<Box
			alignContent="center"
			alignItems="center"
			display="flex"
			flexDirection="column"
			justifyContent="center"
		>
			<Box m="auto">
				<CircularProgress />
			</Box>
		</Box>
	)
}
