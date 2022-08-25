import { Box, IconButton, Popover } from '@mui/material'
import { FC, useState } from 'react'
import InfoIcon from '@mui/icons-material/Info'
import { NavbarInfoContent } from './navbar-info-content'

type Props = {
	instance?: string
	url?: string
	version?: string
}

export const NavbarInfo: FC<Props> = props => {
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
		<Box>
			<Box
				sx={{
					display: { xs: 'none', lg: 'flex' },
					flexDirection: 'column',
					justifyContent: 'space-between',
					minWidth: 250,
				}}
			>
				<NavbarInfoContent {...props} />
			</Box>
			<Box
				sx={{
					display: { xs: 'flex', lg: 'none' },
				}}
			>
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
		</Box>
	)
}
