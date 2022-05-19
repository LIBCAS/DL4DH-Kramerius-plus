import {
	Checkbox,
	Dialog,
	DialogTitle,
	List,
	ListItem,
	ListItemSecondaryAction,
	ListItemText,
	makeStyles,
} from '@material-ui/core'
import { JobEventConfigCreateDto } from 'models/job-event-config-create-dto'
import { KrameriusJob } from 'models/kramerius-job'
import { useEffect, useState } from 'react'

type Props = {
	showDialog: boolean
	onClose: () => void
	onSubmit: (
		krameriusJob: KrameriusJob,
		override: boolean,
		configIndex?: number,
	) => void
	config: JobEventConfigCreateDto
	text: string
}

const useStyles = makeStyles(() => ({
	button: {
		textTransform: 'none',
		padding: '6px 10px',
		fontWeight: 'bold',
	},
}))

export const EnrichmentDialog = ({
	showDialog,
	onClose,
	onSubmit,
	config,
	text,
}: Props) => {
	const [override, setOverride] = useState<boolean>(config.override)

	const classes = useStyles()

	const handleCheckboxToggle = () => {
		setOverride(prevValue => !prevValue)
	}

	const onButtonClick = () => {
		onSubmit(config.krameriusJob, override)
	}

	useEffect(() => {
		setOverride(config.override)
	}, [showDialog])

	return (
		<Dialog fullWidth={true} open={showDialog} onClose={onClose}>
			<DialogTitle id="simple-dialog-title">Konfigurace úlohy</DialogTitle>
			<List>
				<ListItem>
					<ListItemText primary="Typ úlohy" />
					<ListItemSecondaryAction>
						<ListItemText primary={config.krameriusJob} />
					</ListItemSecondaryAction>
				</ListItem>
				<ListItem>
					<ListItemText primary="Přepsat existující" />
					<ListItemSecondaryAction>
						<Checkbox
							checked={override}
							edge="end"
							onChange={handleCheckboxToggle}
						/>
					</ListItemSecondaryAction>
				</ListItem>

				<ListItem button onClick={onButtonClick}>
					<ListItemText
						className={classes.button}
						color="primary"
						primary={text}
					/>
				</ListItem>
			</List>
		</Dialog>
	)
}
