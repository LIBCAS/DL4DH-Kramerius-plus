import { FC } from 'react'
import { Theme, createStyles, makeStyles } from '@material-ui/core/styles'
import MuiAccordion from '@material-ui/core/Accordion'
import AccordionSummary from '@material-ui/core/AccordionSummary'
import AccordionDetails from '@material-ui/core/AccordionDetails'
import Typography from '@material-ui/core/Typography'
import ExpandMoreIcon from '@material-ui/icons/ExpandMore'

const useStyles = makeStyles((theme: Theme) =>
	createStyles({
		root: {
			width: '100%',
		},
		summary: {
			padding: 0,
		},
		details: {
			display: 'flex',
			flexDirection: 'column',
		},
	}),
)

type Props = {
	label: string
}

export const Accordion: FC<Props> = ({ children, label }) => {
	const classes = useStyles()

	return (
		<div className={classes.root}>
			<MuiAccordion elevation={0}>
				<AccordionSummary
					aria-controls="panel1a-content"
					className={classes.summary}
					expandIcon={<ExpandMoreIcon />}
					id="panel1a-header"
				>
					<Typography>{label}</Typography>
				</AccordionSummary>
				<AccordionDetails className={classes.details}>
					{children}
				</AccordionDetails>
			</MuiAccordion>
		</div>
	)
}
