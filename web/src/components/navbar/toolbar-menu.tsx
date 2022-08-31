import { Box, Menu, MenuItem, IconButton } from '@mui/material'
import { FC, useState } from 'react'
import { NavbarInnerItem, NavbarItem } from './navbar'
import MenuIcon from '@mui/icons-material/Menu'
import { useNavigate } from 'react-router-dom'
import { useAuth } from 'components/auth/auth-context'
import { useKeycloak } from '@react-keycloak/web'
import { NavbarMenuItem } from './navbar-menu-item'

export const ToolbarMenu: FC<{
	pages: NavbarItem[]
	lgWidth: string
	xsWidth: string
}> = ({ pages, lgWidth, xsWidth }) => {
	const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null)
	const navigate = useNavigate()
	const { auth } = useAuth()
	const { keycloak } = useKeycloak()

	const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
		setAnchorElNav(event.currentTarget)
	}
	const handleCloseNavMenu = () => {
		setAnchorElNav(null)
	}

	const handleNavMenuClick = (link: string) => () => {
		setAnchorElNav(null)
		navigate(link)
	}

	const logout = () => {
		keycloak.logout()
	}

	function flatten(arr: NavbarItem[]): NavbarInnerItem[] {
		return arr.reduce(function (
			flat: NavbarInnerItem[],
			toFlatten: NavbarItem,
		): NavbarInnerItem[] {
			if (toFlatten.children) {
				return flat.concat(toFlatten.children)
			} else {
				flat.push({
					name: toFlatten.name,
					label: toFlatten.label,
					link: toFlatten.link ?? '/',
					onlyAuthenticated: toFlatten.onlyAuthenticated,
				})
				return flat
			}
		},
		[])
	}

	return (
		<Box sx={{ width: { lg: lgWidth, xs: xsWidth } }}>
			<Box
				sx={{
					flexGrow: 1,
					display: { lg: 'flex', xs: 'none' },
					justifyContent: 'center',
					alignItems: 'center',
				}}
			>
				{pages
					.filter(page => !page.onlyAuthenticated || auth)
					.map(page => (
						<NavbarMenuItem key={page.name} item={page} />
					))}
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
					{flatten(pages)
						.filter(page => !page.onlyAuthenticated || auth)
						.map(page => (
							<MenuItem
								key={page.name}
								onClick={handleNavMenuClick(page.link || '/')}
							>
								{page.label}
							</MenuItem>
						))}
					{auth && <MenuItem onClick={logout}>Odhlásit se</MenuItem>}
				</Menu>
			</Box>
		</Box>
	)
}
