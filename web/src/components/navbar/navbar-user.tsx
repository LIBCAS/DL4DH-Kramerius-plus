import PersonIcon from '@mui/icons-material/Person'
import { Box, Button, Menu, MenuItem } from '@mui/material'
import { useKeycloak } from '@react-keycloak/web'
import { FC, useState } from 'react'

export const NavbarUser: FC<{ width: string }> = ({ width }) => {
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

	if (keycloak.authenticated) {
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
					{keycloak.tokenParsed?.preferred_username}
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
