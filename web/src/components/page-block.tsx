import { Typography } from '@mui/material'
import { Box } from '@mui/system'
import { FC } from 'react'

export const PageBlock: FC<{ title: string }> = ({ title, children }) => {
	return (
		<Box p={1}>
			<Box pb={3}>
				<Typography variant="h6">{title}</Typography>
			</Box>
			<Box>{children}</Box>
		</Box>
	)
}
