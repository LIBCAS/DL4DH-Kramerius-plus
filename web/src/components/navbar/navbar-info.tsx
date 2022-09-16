import { Box, Button, IconButton, Popover, Typography } from '@mui/material'
import { FC, useState } from 'react'
import { NavbarInfoContent } from './navbar-info-content'
import InfoIcon from '@mui/icons-material/Info'
import { Link } from 'react-router-dom'

type Props = {
	instance?: string
	url?: string
	version?: string
}

export const NavbarInfo: FC<{
	props: Props
	lgWidth: string
	xsWidth: string
}> = ({ props, lgWidth, xsWidth }) => {
	const [anchorEl, setAnchorEl] = useState<HTMLButtonElement | null>(null)

	const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
		setAnchorEl(event.currentTarget)
	}

	const handleClose = () => {
		setAnchorEl(null)
	}

	const open = Boolean(anchorEl)
	const id = open ? 'simple-popover' : undefined

	return (
		<Box sx={{ width: { lg: lgWidth, xs: xsWidth }, display: 'flex' }}>
			<Button color="inherit" component={Link} to="/" variant="text">
				<Typography
					sx={{
						mr: 1,
						fontFamily: 'monospace',
						fontWeight: 500,
						letterSpacing: '.1rem',
						color: 'inherit',
						textDecoration: 'none',
					}}
					variant="h6"
				>
					Kramerius+ Client
				</Typography>
			</Button>
			<IconButton aria-describedby={id} onClick={handleClick}>
				<InfoIcon
					sx={{
						color: 'white',
					}}
				/>
			</IconButton>
			<Popover
				anchorEl={anchorEl}
				anchorOrigin={{
					vertical: 'bottom',
					horizontal: 'left',
				}}
				id={id}
				open={open}
				onClose={handleClose}
			>
				<NavbarInfoContent {...props} />
			</Popover>
		</Box>
	)
}
