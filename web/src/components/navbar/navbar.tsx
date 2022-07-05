import { Link } from 'react-router-dom'
import { Box, AppBar, Toolbar, Typography, Button } from '@mui/material'
import ALink from '@mui/material/Link'

import { useNavbar } from './navbar-hook'

export const Navbar = () => {
	const { instance, url, version } = useNavbar()

	return (
		<AppBar>
			<Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
				<Box sx={{ display: 'flex' }}>
					<Typography sx={{ marginRight: '40px' }} variant="h6">
						Kramerius+ Client
					</Typography>
					<Button
						color="inherit"
						component={Link}
						sx={{ marginRight: 2 }}
						to="/"
					>
						Obohacení
					</Button>
					<Button color="inherit" component={Link} to="/jobs/enriching">
						Úlohy obohacení
					</Button>
					<Button color="inherit" component={Link} to="/jobs/exporting">
						Úlohy exportování
					</Button>
					<Button color="inherit" component={Link} to="/publications">
						Publikace
					</Button>
					<Button color="inherit" component={Link} to="/exports">
						Exporty
					</Button>
				</Box>
				<Box sx={{ display: 'flex', flexDirection: 'column' }}>
					<Typography component="span" sx={{ fontSize: 14 }}>
						Instance: {instance}
					</Typography>
					<Box
						display="flex"
						justifyContent="space-between"
						sx={{ minWidth: 250 }}
					>
						<Typography component="span" sx={{ fontSize: 14 }}>
							Url: {url}
						</Typography>
						<Typography component="span" sx={{ fontSize: 14 }}>
							<ALink
								color="inherit"
								href="https://github.com/LIBCAS/DL4DH-Kramerius-plus/wiki/Changelog"
							>
								Verze: {version}
							</ALink>
						</Typography>
					</Box>
				</Box>
			</Toolbar>
		</AppBar>
	)
}
