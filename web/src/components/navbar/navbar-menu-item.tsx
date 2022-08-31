import { Button, Menu, MenuItem, Box } from '@mui/material'
import { useAuth } from 'components/auth/auth-context'
import { FC, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { NavbarItem } from './navbar'

export const NavbarMenuItem: FC<{
	item: NavbarItem
}> = ({ item }) => {
	const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)
	const navigate = useNavigate()
	const open = Boolean(anchorEl)

	const { auth } = useAuth()

	const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
		setAnchorEl(event.currentTarget)
	}

	const handleClose = () => {
		setAnchorEl(null)
	}

	const handleMenuItemClick = (link: string) => () => {
		setAnchorEl(null)
		navigate(link)
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
						.filter(pageItem => !pageItem.onlyAuthenticated || auth)
						.map(pageItem => (
							<MenuItem
								key={pageItem.name}
								onClick={handleMenuItemClick(pageItem.link)}
							>
								{pageItem.label}
							</MenuItem>
						))}
				</Menu>
			</Box>
		)
	} else {
		return (
			<Button
				color="inherit"
				component={Link}
				to={item?.link ?? '/'}
				variant="text"
			>
				{item.label}
			</Button>
		)
	}
}
