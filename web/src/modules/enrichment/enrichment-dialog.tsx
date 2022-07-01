import {
	Checkbox,
	Dialog,
	DialogTitle,
	List,
	ListItem,
	ListItemSecondaryAction,
	ListItemText,
	makeStyles,
	MenuItem,
} from '@material-ui/core'
import {
	Select,
	SelectChangeEvent,
	FormControl,
	InputLabel,
} from '@mui/material'
import { EnrichmentKrameriusJob } from 'enums/enrichment-kramerius-job'
import { EnrichmentJobEventConfigCreateDto } from 'models/job/config/dto/enrichment-job-event-config-create-dto'
import { EnrichmentJobEventConfigType } from 'models/job/config/dto/enrichment/enrichment-job-event-config-type'
import { ExternalEnrichmentJobEventConfigCreateDto } from 'models/job/config/dto/enrichment/external-enrichment-job-event-config-create-dto'
import { useEffect, useState } from 'react'
import { MissingAltoStrategy } from '../../enums/missing-alto-strategy'

type Props = {
	showDialog: boolean
	onClose: () => void
	onSubmit: (config: EnrichmentJobEventConfigType, configIndex?: number) => void
	existingConfig: EnrichmentJobEventConfigCreateDto
	text: string
	configIndex?: number
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
	existingConfig,
	text,
	configIndex,
}: Props) => {
	const [config, setConfig] = useState<EnrichmentJobEventConfigType>({
		...existingConfig,
	})

	const classes = useStyles()

	const handleCheckboxToggle = () => {
		setConfig(prevValue => ({ ...prevValue, override: !prevValue.override }))
	}

	const onButtonClick = () => {
		onSubmit(config, configIndex)
	}

	const handleStrategyChange = (event: SelectChangeEvent) => {
		setConfig({
			...config,
			missingAltoOption: event.target.value as MissingAltoStrategy,
		})
	}

	useEffect(() => {
		setConfig({ ...existingConfig })
	}, [showDialog, existingConfig])

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
							checked={config.override}
							edge="end"
							onChange={handleCheckboxToggle}
						/>
					</ListItemSecondaryAction>
				</ListItem>
				{config.krameriusJob === EnrichmentKrameriusJob.ENRICHMENT_EXTERNAL && (
					<ListItem>
						<ListItemText primary="Strategie při chybějícím ALTO" />
						<ListItemSecondaryAction>
							<FormControl size="small" sx={{ m: 1, minWidth: 250 }}>
								<InputLabel>Strategie</InputLabel>
								<Select
									id="demo-select-small"
									label="Strategie"
									labelId="demo-select-small"
									value={
										(config as ExternalEnrichmentJobEventConfigCreateDto)
											.missingAltoOption
									}
									onChange={handleStrategyChange}
								>
									<MenuItem value={MissingAltoStrategy.FAIL_IF_ALL_MISS}>
										Chyba když všechny chybí
									</MenuItem>
									<MenuItem value={MissingAltoStrategy.FAIL_IF_ONE_MISS}>
										Chyba když jeden chybí
									</MenuItem>
									<MenuItem value={MissingAltoStrategy.SKIP}>
										Vždy přeskočit
									</MenuItem>
								</Select>
							</FormControl>
						</ListItemSecondaryAction>
					</ListItem>
				)}

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
