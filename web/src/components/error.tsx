import { Alert, AlertTitle, Box } from '@mui/material'
import { FC } from 'react'

export const CustomErrorComponent: FC<{ error?: any }> = ({ error }) => {
	return (
		<Box
			alignContent="center"
			alignItems="center"
			display="flex"
			flexDirection="column"
			justifyContent="center"
		>
			<Box m="auto">
				<Alert severity="error">
					<AlertTitle>Při načítání dat došlo k chybě</AlertTitle>
					<strong>{error}</strong>
				</Alert>
			</Box>
		</Box>
	)
}
