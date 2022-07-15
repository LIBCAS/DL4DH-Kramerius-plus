import { Link } from 'react-router-dom'
import AppBar from '@mui/material/AppBar'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'
import Button from '@material-ui/core/Button'
import ALink from '@mui/material/Link'

import { useNavbar } from './navbar-hook'
import { Box } from '@mui/system'

const useStyles = makeStyles(theme => ({
	toolbar: {
		display: 'flex',
		justifyContent: 'space-between',
	},
	menuButton: {
		marginRight: theme.spacing(2),
	},
	title: {
		marginRight: 40,
	},
	instanceInfo: {
		fontSize: 14,
	},
}))

export const Navbar = () => {
	const classes = useStyles()

	const { instance, url, version } = useNavbar()

	return (
		<AppBar>
			<Toolbar className={classes.toolbar}>
				<div style={{ display: 'flex' }}>
					<Typography className={classes.title} variant="h6">
						Kramerius+ Client
					</Typography>
					<Button
						className={classes.menuButton}
						color="inherit"
						component={Link}
						to="/"
					>
						Obohacení
					</Button>
					<Button color="inherit" component={Link} to="/jobs/enriching">
						Úlohy obohacení
					</Button>
					<Button color="inherit" component={Link} to="/publications">
						Publikace
					</Button>
					<Button color="inherit" component={Link} to="/jobs/exporting">
						Úlohy exportování
					</Button>
					<Button color="inherit" component={Link} to="/exports">
						Exporty
					</Button>
				</div>
				<div style={{ display: 'flex', flexDirection: 'column' }}>
					<Typography className={classes.instanceInfo} component="span">
						Instance: {instance}
					</Typography>
					<Box
						display="flex"
						justifyContent="space-between"
						sx={{ minWidth: 250 }}
					>
						<Typography className={classes.instanceInfo} component="span">
							Url: {url}
						</Typography>
						<Typography className={classes.instanceInfo} component="span">
							<ALink
								color="inherit"
								href="https://github.com/LIBCAS/DL4DH-Kramerius-plus/wiki/Changelog"
							>
								Verze: {version}
							</ALink>
						</Typography>
					</Box>
				</div>
			</Toolbar>
		</AppBar>
	)
}
