import { Link } from 'react-router-dom'
import AppBar from '@material-ui/core/AppBar'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'
import Button from '@material-ui/core/Button'

import { useNavbar } from './navbar-hook'

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

	const { instance, url } = useNavbar()

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
					<Button color="inherit" component={Link} to="/jobs/exporting">
						Úlohy exportování
					</Button>
					<Button color="inherit" component={Link} to="/publications">
						Publikace
					</Button>
					<Button color="inherit" component={Link} to="/export">
						Exporty
					</Button>
				</div>
				<div style={{ display: 'flex', flexDirection: 'column' }}>
					<Typography className={classes.instanceInfo} component="span">
						Instance: {instance}
					</Typography>
					<Typography className={classes.instanceInfo} component="span">
						Url: {url}
					</Typography>
				</div>
			</Toolbar>
		</AppBar>
	)
}
