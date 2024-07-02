import MenuIcon from '@mui/icons-material/Menu'
import {Box, IconButton, Menu, MenuItem} from '@mui/material'
import {useKeycloak} from '@react-keycloak/web'
import {KEYCLOAK_TOKEN} from 'keycloak'
import {FC, useState} from 'react'
import {Link} from 'react-router-dom'
import {NavbarInnerItem, NavbarItem} from './navbar'
import {NavbarMenuItem} from './navbar-menu-item'

export const ToolbarMenu: FC<{
	pages: NavbarItem[]
	lgWidth: string
	xsWidth: string
}> = ({ pages, lgWidth, xsWidth }) => {
	const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null)
	const { keycloak } = useKeycloak()

	const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
		setAnchorElNav(event.currentTarget)
	}
	const handleCloseNavMenu = () => {
		setAnchorElNav(null)
	}

	const logout = () => {
		keycloak.logout()
		localStorage.removeItem(KEYCLOAK_TOKEN)
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
					path: toFlatten.path,
					onlyAuthenticated: toFlatten.onlyAuthenticated,
				})
				return flat
			}
		},
		[])
	}

	const canAccess = (page: NavbarItem) => {
		const userRoles = keycloak?.authenticated
			? keycloak.tokenParsed?.realm_access?.roles
			: []

		const hasAdminRole = userRoles?.includes('dl4dh-admin')

		return !page.onlyAuthenticated || (keycloak.authenticated && hasAdminRole)
	}

	return (
		<Box
			sx={{
				width: { lg: lgWidth, xs: xsWidth },
				display: 'flex',
				justifyContent: 'center',
			}}
		>
			<Box
				sx={{
					display: { lg: 'flex', xs: 'none' },
					justifyContent: 'space-between',
					alignItems: 'center',
					width: '70%',
				}}
			>
				{pages
					.filter(page => canAccess(page))
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
						.filter(page => !page.onlyAuthenticated || keycloak.authenticated)
						.map(page => (
							<MenuItem
								key={page.name}
								component={Link}
								to={page.path}
								onClick={handleCloseNavMenu}
							>
								{page.label}
							</MenuItem>
						))}
					{!!keycloak.authenticated && (
						<MenuItem onClick={logout}>Odhlásit se</MenuItem>
					)}
				</Menu>
			</Box>
		</Box>
	)
}
