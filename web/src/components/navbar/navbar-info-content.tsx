import { Box, Typography, Link } from '@mui/material'
import { FC } from 'react'

type Props = {
	instance?: string
	url?: string
	version?: string
}

export const NavbarInfoContent: FC<Props> = ({ instance, url, version }) => {
	return (
		<Box
			sx={{
				display: 'flex',
				flexDirection: 'column',
				justifyContent: 'space-between',
				p: 2,
				minWidth: 250,
			}}
		>
			<Box display="flex" justifyContent="space-between">
				<Typography component="span" fontSize={12}>
					Instance:
				</Typography>
				<Typography component="span" fontSize={12}>
					{instance}
				</Typography>
			</Box>
			<Box display="flex" justifyContent="space-between">
				<Typography component="span" fontSize={12}>
					Url: {url}
				</Typography>
				<Typography component="span" fontSize={12}>
					<Link
						color="inherit"
						href="https://github.com/LIBCAS/DL4DH-Kramerius-plus/wiki/Changelog"
					>
						Verze: {version}
					</Link>
				</Typography>
			</Box>
		</Box>
	)
}
