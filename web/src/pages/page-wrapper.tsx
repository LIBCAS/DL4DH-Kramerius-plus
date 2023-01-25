import { Box } from '@mui/material'
import { FC } from 'react'
import { PrivateRoute } from 'utils/private-route'

export const PageWrapper: FC<{
	requireAuth?: boolean
	children: JSX.Element
}> = ({ requireAuth, children }) => {
	const wrapped = <Box sx={{ p: 3 }}>{children}</Box>
	const secured = requireAuth ? <PrivateRoute>{wrapped}</PrivateRoute> : wrapped

	return secured
}
