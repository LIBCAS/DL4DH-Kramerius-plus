import { Paper } from '@mui/material'
import { makeStyles } from '@material-ui/core/styles'
import { EnrichmentForm } from '../modules/enrichment/enrichment-form'

const useStyles = makeStyles(() => ({
	paper: {
		padding: '10px 24px',
		minHeight: 140,
	},
}))

export const Enrichment = () => {
	const classes = useStyles()
	return (
		<Paper className={classes.paper}>
			<EnrichmentForm />
		</Paper>
	)
}
