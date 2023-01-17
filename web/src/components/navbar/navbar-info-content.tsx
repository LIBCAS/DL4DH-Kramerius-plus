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
					<Link color="inherit" href={url} target="_blank">
						{instance}
					</Link>
				</Typography>
			</Box>
			<Box display="flex" justifyContent="space-between">
				<Typography component="span" fontSize={12}>
					Verze:
				</Typography>
				<Typography component="span" fontSize={12}>
					{version}
				</Typography>
			</Box>
			<Box display="flex" justifyContent="space-between">
				<Typography component="span" fontSize={12}>
					Odkazy:
				</Typography>
				<Typography component="span" fontSize={12}>
					<Link
						color="inherit"
						href="https://github.com/LIBCAS/DL4DH-Kramerius-plus/wiki/Webov%C3%A1-aplikace"
						target="_blank"
					>
						Dokumentace Kramerius+
					</Link>
				</Typography>
			</Box>
			<Box display="flex" justifyContent="space-between">
				<Typography component="span" fontSize={12}></Typography>
				<Typography component="span" fontSize={12}>
					<Link
						color="inherit"
						href="https://github.com/LIBCAS/DL4DH"
						target="_blank"
					>
						Informace o projektu DL4DH
					</Link>
				</Typography>
			</Box>
		</Box>
	)
}
