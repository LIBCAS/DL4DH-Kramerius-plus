import { Box, Button, Menu, MenuItem, IconButton } from '@mui/material'
import { FC, useState } from 'react'
import { Page } from './navbar'
import MenuIcon from '@mui/icons-material/Menu'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from 'components/auth/auth-context'
import { useKeycloak } from '@react-keycloak/web'

type Props = {
	pages: Page[]
}

export const ToolbarMenu: FC<Props> = ({ pages }) => {
	const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null)
	const navigate = useNavigate()
	const { auth } = useAuth()
	const { keycloak } = useKeycloak()

	const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
		setAnchorElNav(event.currentTarget)
	}
	const handleCloseNavMenu = (link: string) => () => {
		setAnchorElNav(null)
		navigate(link)
	}

	const logout = () => {
		keycloak.logout()
	}

	return (
		<Box>
			<Box sx={{ ml: 2, flexGrow: 1, display: { lg: 'flex', xs: 'none' } }}>
				{pages
					.filter(page => !page.onlyAuthenticated || auth)
					.map(page => (
						<Button
							key={page.name}
							color="inherit"
							component={Link}
							to={page.link}
							variant="text"
						>
							{page.label}
						</Button>
					))}
				{auth && (
					<Button
						color="inherit"
						component={Link}
						to="/"
						variant="text"
						onClick={logout}
					>
						Odhlásit se
					</Button>
				)}
			</Box>
			<Box
				sx={{
					width: 1,
					ml: 2,
					flexGrow: 1,
					display: { lg: 'none', xs: 'flex' },
				}}
			>
				<IconButton
					aria-controls="menu-appbar"
					aria-haspopup="true"
					aria-label="account of current user"
					color="inherit"
					size="large"
					sx={{
						justifyContent: 'left',
					}}
					onClick={handleOpenNavMenu}
				>
					<MenuIcon />
				</IconButton>
				<Menu
					anchorEl={anchorElNav}
					anchorOrigin={{
						vertical: 'bottom',
						horizontal: 'left',
					}}
					id="menu-appbar"
					keepMounted
					open={Boolean(anchorElNav)}
					sx={{
						display: 'block',
					}}
					transformOrigin={{
						vertical: 'top',
						horizontal: 'left',
					}}
					onClose={handleCloseNavMenu}
				>
					{pages
						.filter(page => !page.onlyAuthenticated || auth)
						.map(page => (
							<MenuItem key={page.name} onClick={handleCloseNavMenu(page.link)}>
								{page.label}
							</MenuItem>
						))}
					{auth && <MenuItem onClick={logout}>Odhlásit se</MenuItem>}
				</Menu>
			</Box>
		</Box>
	)
}
