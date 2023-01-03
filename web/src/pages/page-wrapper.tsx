import { Box } from '@mui/material'
import { FC } from 'react'
import { Navigate } from 'react-router-dom'

export const PageWrapper: FC<{
	requireAuth?: boolean
	children: JSX.Element
}> = ({ requireAuth, children }) => {
	const token = localStorage.getItem('token')

	const wrapped = <Box sx={{ p: 3 }}>{children}</Box>

	return token || !requireAuth ? wrapped : <Navigate to={'/'} />
}
