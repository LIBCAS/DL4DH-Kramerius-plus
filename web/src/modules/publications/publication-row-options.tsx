import { IconButton, ListItemIcon, Menu, MenuItem } from '@mui/material'
import { FC, useState } from 'react'
import MoreVertIcon from '@mui/icons-material/MoreVert'

export type GridOption = {
	icon: JSX.Element
	label: string
	onClick?: () => void
	href?: string
}

export const PublicationRowOptions: FC<{ options: GridOption[] }> = ({
	options,
}) => {
	const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)
	const open = Boolean(anchorEl)
	const handleIconClick = (event: React.MouseEvent<HTMLElement>) => {
		event.stopPropagation()
		setAnchorEl(event.currentTarget)
	}

	const handleClose = () => {
		setAnchorEl(null)
	}

	const handleOptionClick = (onClick: () => void) => () => {
		setAnchorEl(null)
		onClick()
	}

	return (
		<div>
			<IconButton
				aria-controls={open ? 'long-menu' : undefined}
				aria-expanded={open ? 'true' : undefined}
				aria-haspopup="true"
				aria-label="more"
				id="long-button"
				onClick={handleIconClick}
			>
				<MoreVertIcon />
			</IconButton>
			<Menu
				anchorEl={anchorEl}
				id="long-menu"
				open={open}
				onClose={handleClose}
			>
				{options.map((option, index) => {
					if (option.onClick) {
						return (
							<MenuItem key={index} onClick={handleOptionClick(option.onClick)}>
								<ListItemIcon>{option.icon}</ListItemIcon>
								{option.label}
							</MenuItem>
						)
					}

					if (option.href) {
						return (
							<MenuItem key={index} component="a" href={option.href}>
								<ListItemIcon>{option.icon}</ListItemIcon>
								{option.label}
							</MenuItem>
						)
					}
				})}
			</Menu>
		</div>
	)
}
