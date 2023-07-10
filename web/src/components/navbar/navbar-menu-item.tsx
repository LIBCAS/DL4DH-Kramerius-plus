import { Box, Button, Menu, MenuItem } from '@mui/material'
import { useKeycloak } from '@react-keycloak/web'
import { FC, useState } from 'react'
import { Link } from 'react-router-dom'
import { NavbarItem } from './navbar'

export const NavbarMenuItem: FC<{
	item: NavbarItem
}> = ({ item }) => {
	const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)
	const open = Boolean(anchorEl)
	const { keycloak } = useKeycloak()

	const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
		setAnchorEl(event.currentTarget)
	}

	const handleClose = () => {
		setAnchorEl(null)
	}

	if (item.children) {
		return (
			<Box sx={{ mx: 2 }}>
				<Button
					aria-controls={open ? 'basic-menu' : undefined}
					aria-expanded={open ? 'true' : undefined}
					aria-haspopup="true"
					color="inherit"
					id="basic-button"
					onClick={handleClick}
				>
					{item.label}
				</Button>
				<Menu
					MenuListProps={{ 'aria-labelledby': 'basic-button' }}
					anchorEl={anchorEl}
					open={open}
					onClose={handleClose}
				>
					{item.children
						.filter(
							pageItem => !pageItem.onlyAuthenticated || keycloak.authenticated,
						)
						.map(pageItem => (
							<MenuItem
								key={pageItem.name}
								component={Link}
								to={pageItem.path}
								onClick={handleClose}
							>
								{pageItem.label}
							</MenuItem>
						))}
				</Menu>
			</Box>
		)
	} else {
		return (
			// <Button
			// 	color="inherit"
			// 	component={Link}
			// 	to={{ pathName: item?.link ?? '/', search: '?page=0&pageSize=10' }}
			// 	variant="text"
			// >
			// 	{item.label}
			// </Button>
			<Box>
				<Link
					style={{ textDecoration: 'none', color: 'white' }}
					to={{ pathname: item.path, search: item.search }}
				>
					<Button color="inherit" variant="text">
						{item.label}
					</Button>
				</Link>
			</Box>
		)
	}
}
