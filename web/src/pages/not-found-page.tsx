import { Box, Grid } from '@mui/material'
import { FC } from 'react'
import { PageWrapper } from './page-wrapper'

export const NotFoundPage: FC = () => {
	return (
		<PageWrapper>
			<Grid container justifyContent="center">
				<Box>Not Found</Box>
			</Grid>
		</PageWrapper>
	)
}
