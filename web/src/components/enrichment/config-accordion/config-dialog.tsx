import { TextField } from '@material-ui/core'
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
	Input,
	FormGroup,
	FormControlLabel,
	DialogContentText,
	Typography,
} from '@mui/material'
import { KrameriusJobMapping } from '../../../components/mappings/kramerius-job-mapping'
import { MissingAltoStrategy } from '../../../enums/missing-alto-strategy'
import { EnrichmentJobEventConfig } from '../../../models/job/config/enrichment-job-event-config'
import { CurrentConfig } from '../enrichment-form'
import { MissingAltoStrategySelect } from './missing-alto-strategy-select'

type Props = {
	open: boolean
	currentConfig: CurrentConfig
	onOverrideChange: () => void
	onSubmit: () => void
	onClose: () => void
}

export const ConfigDialog = ({
	open,
	currentConfig,
	onOverrideChange,
	onSubmit,
	onClose,
}: Props) => {
	const isNew = () => {
		return currentConfig?.index === undefined
	}

	return (
		<Dialog fullWidth open={open} sx={{ p: 20 }} onClose={onClose}>
			{/* <DialogTitle>
				{isNew() ? 'Přidat novou' : 'Upravit'} konfiguraci
			</DialogTitle>
			<DialogContent>
				<Typography variant="h5">
					{KrameriusJobMapping[currentConfig.config.krameriusJob]}
				</Typography>
				<FormGroup>
					<FormControlLabel control={<Checkbox />} label="Přepsat existující" />
					<FormControlLabel
						control={<TextField type="number" />}
						label="Tolerance chyb v publikacích"
					/>
					<FormControlLabel
						control={<TextField type="number" />}
						label="Tolerance chyb v stránkach"
					/>
				</FormGroup>
			</DialogContent>
			<DialogActions disableSpacing={true}>
				<Button
					sx={{ marginBottom: 1, marginRight: 2 }}
					variant="contained"
					onClick={onSubmit}
				>
					{isNew() ? 'Přidat' : 'Upravit'}
				</Button>
			</DialogActions> */}
		</Dialog>
	)
}
