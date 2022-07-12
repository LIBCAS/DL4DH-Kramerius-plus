import {
	Dialog,
	DialogContent,
	DialogTitle,
	DialogActions,
	Button,
	Checkbox,
	List,
	ListItem,
	ListItemSecondaryAction,
	ListItemText,
	SelectChangeEvent,
} from '@mui/material'
import { KrameriusJobMapping } from '../../../components/mappings/kramerius-job-mapping'
import { MissingAltoStrategy } from '../../../enums/missing-alto-strategy'
import { EnrichmentJobEventConfig } from '../../../models/job/config/enrichment/enrichment-job-event-config'
import { ExternalEnrichmentJobEventConfig } from '../../../models/job/config/enrichment/external-enrichment-job-event-config'
import { CurrentConfig } from '../enrichment-form'
import { MissingAltoStrategySelect } from './missing-alto-strategy-select'

type Props = {
	open: boolean
	currentConfig: CurrentConfig
	onOverrideChange: () => void
	onStrategyChange: (value: MissingAltoStrategy) => void
	onSubmit: () => void
	onClose: () => void
}

export const ConfigDialog = ({
	open,
	currentConfig,
	onOverrideChange,
	onStrategyChange,
	onSubmit,
	onClose,
}: Props) => {
	function isNew() {
		return currentConfig?.index === undefined
	}

	function isExternalConfig(
		config: EnrichmentJobEventConfig,
	): config is ExternalEnrichmentJobEventConfig {
		return 'missingAltoOption' in config
	}

	const handleStrategyChange = (
		event: SelectChangeEvent<MissingAltoStrategy>,
	) => {
		onStrategyChange(event.target.value as MissingAltoStrategy)
	}

	return (
		<Dialog fullWidth open={open} sx={{ p: 20 }} onClose={onClose}>
			<DialogTitle>
				{isNew() ? 'Přidat novou' : 'Upravit'} konfiguraci
			</DialogTitle>
			<DialogContent>
				<List>
					<ListItem>
						<ListItemText primary="Typ úlohy" />
						<ListItemSecondaryAction>
							<ListItemText
								primary={
									KrameriusJobMapping[currentConfig?.config.krameriusJob]
								}
							/>
						</ListItemSecondaryAction>
					</ListItem>
					<ListItem>
						<ListItemText primary="Přepsat existující" />
						<ListItemSecondaryAction>
							<Checkbox
								checked={currentConfig.config.override}
								edge="end"
								onChange={onOverrideChange}
							/>
						</ListItemSecondaryAction>
					</ListItem>
					{isExternalConfig(currentConfig.config) && (
						<ListItem>
							<ListItemText primary="Strategie při chybějícím ALTO" />
							<ListItemSecondaryAction>
								<MissingAltoStrategySelect
									defaultValue={currentConfig.config.missingAltoOption}
									onChange={handleStrategyChange}
								/>
							</ListItemSecondaryAction>
						</ListItem>
					)}
				</List>
			</DialogContent>
			<DialogActions disableSpacing={true}>
				<Button
					sx={{ marginBottom: 1, marginRight: 2 }}
					variant="contained"
					onClick={onSubmit}
				>
					{isNew() ? 'Přidat' : 'Upravit'}
				</Button>
			</DialogActions>
		</Dialog>
	)
}
