import { Box, Button, Menu, MenuItem } from '@mui/material'
import { FC, useState } from 'react'
import { useAuth } from 'components/auth/auth-context'
import PersonIcon from '@mui/icons-material/Person'
import { useKeycloak } from '@react-keycloak/web'

export const NavbarUser: FC<{ width: string }> = ({ width }) => {
	const { auth, user } = useAuth()
	const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)

	const { keycloak } = useKeycloak()

	const handleClose = () => {
		setAnchorEl(null)
	}

	const logout = () => {
		keycloak.logout()
	}

	const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
		setAnchorEl(event.currentTarget)
	}

	if (auth) {
		return (
			<Box
				sx={{
					ml: 1,
					display: 'flex',
					alignItems: 'flex-end',
					justifyContent: 'end',
					width: width,
				}}
			>
				<Button
					aria-controls="menu-appbar"
					aria-haspopup="true"
					aria-label="account of current user"
					color="inherit"
					variant="text"
					onClick={handleClick}
				>
					<PersonIcon sx={{ color: 'white', mr: 2 }} />
					{user?.username}
				</Button>
				<Menu
					MenuListProps={{ 'aria-labelledby': 'basic-button' }}
					anchorEl={anchorEl}
					open={Boolean(anchorEl)}
					onClose={handleClose}
				>
					<MenuItem onClick={logout}>Odhlásit</MenuItem>
				</Menu>
			</Box>
		)
	} else {
		return <Box sx={{ width: width }}></Box>
	}
}
