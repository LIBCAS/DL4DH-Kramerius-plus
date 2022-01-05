import React from 'react'
import { AppBar, Toolbar, Typography } from '@material-ui/core'
import { makeStyles } from '@material-ui/core/styles'
import Button from '@material-ui/core/Button'
import { Link } from 'react-router-dom'

const useStyles = makeStyles(theme => ({
	menuButton: {
		marginRight: theme.spacing(2),
	},
	title: {
		marginRight: 40,
	},
}))

export const Navbar = () => {
	const classes = useStyles()

	return (
		<AppBar>
			<Toolbar>
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
				<Button color="inherit" component={Link} to="/export">
					Export
				</Button>
			</Toolbar>
		</AppBar>
	)
}
